package org.javaup.ai.chatagent.tool;

import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.tools.ToolContextHelper;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.javaup.ai.chatagent.config.BochaSearchProperties;
import org.javaup.ai.chatagent.model.SearchReference;
import org.javaup.ai.chatagent.model.debug.ChatDebugTrace;
import org.javaup.ai.chatagent.model.debug.ChatToolTrace;
import org.javaup.ai.chatagent.support.ChatContextKeys;
import org.javaup.ai.chatagent.support.RestClientFactorySupport;
import org.javaup.ai.chatagent.support.SinkEmitHelper;
import org.javaup.ai.chatagent.support.StreamEventMetadata;
import org.javaup.ai.chatagent.support.StreamEventWriter;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 博查（Bocha）联网搜索工具。
 * 实现模式与 {@link TavilySearchTool} 完全对齐：
 * 复用统一的工具链路（ToolTrace / REFERENCES 汇集 / thinking 事件流 / RestClient 超时治理），
 * 便于中文场景下作为 Tavily 的替代或补充检索源。
 */
@Slf4j
@Component
public class BochaSearchTool {

    private static final Set<String> ALLOWED_FRESHNESS =
        Set.of("noLimit", "oneDay", "oneWeek", "oneMonth", "oneYear");

    private final BochaSearchProperties properties;
    private final StreamEventWriter streamEventWriter;
    private final RestClient restClient;

    public BochaSearchTool(BochaSearchProperties properties, StreamEventWriter streamEventWriter) {
        this.properties = properties;
        this.streamEventWriter = streamEventWriter;
        this.restClient = RestClientFactorySupport.create(
            properties.getBaseUrl(),
            properties.getConnectTimeoutMs(),
            properties.getReadTimeoutMs()
        );
    }

    public BochaSearchToolResult search(BochaSearchRequest request, ToolContext toolContext) {

        String rawQuery = request != null && StrUtil.isNotBlank(request.getQuery()) ? request.getQuery().trim() : "";
        if (StrUtil.isBlank(rawQuery)) {
            throw new IllegalArgumentException("query 不能为空");
        }
        if (!properties.isEnabled()) {
            throw new IllegalStateException("博查搜索工具当前已禁用");
        }
        if (StrUtil.isBlank(properties.getApiKey())) {
            throw new IllegalStateException("博查 API Key 未配置");
        }

        long startTime = System.currentTimeMillis();
        String freshness = resolveFreshness(request);
        ChatToolTrace toolTrace = registerToolTrace(toolContext, ChatToolTrace.builder()
            .toolName("bocha_search")
            .status("RUNNING")
            .inputSummary(rawQuery)
            .topic(freshness)
            .build());
        markToolUsed(toolContext, "bocha_search");
        publishThinking(toolContext, "正在使用博查搜索: " + rawQuery);

        try {
            BochaSearchApiResponse response = restClient.post()
                .uri(properties.getSearchPath())
                .header("Authorization", "Bearer " + properties.getApiKey())
                .body(new BochaSearchApiRequest(
                    rawQuery,
                    freshness,
                    properties.isSummary(),
                    request != null && request.getMaxResults() != null && request.getMaxResults() > 0
                        ? request.getMaxResults()
                        : properties.getMaxResults()
                ))
                .retrieve()
                .body(BochaSearchApiResponse.class);

            if (response == null || response.data() == null || response.data().webPages() == null) {
                throw new IllegalStateException("博查返回空响应");
            }

            List<SearchReference> references = new ArrayList<>();
            if (response.data().webPages().value() != null) {
                for (BochaResultItem item : response.data().webPages().value()) {
                    if (StrUtil.isBlank(item.url())) {
                        continue;
                    }
                    references.add(new SearchReference(
                        item.name(),
                        item.url(),
                        StrUtil.isNotBlank(item.summary()) ? item.summary() : StrUtil.blankToDefault(item.snippet(), "")
                    ));
                }
            }

            appendReferences(toolContext, references);
            publishThinking(toolContext, "博查搜索完成，找到 " + references.size() + " 条候选来源");
            completeToolTrace(toolTrace, references.size(), startTime);

            return new BochaSearchToolResult(rawQuery, List.copyOf(references));
        }
        catch (RuntimeException exception) {
            failToolTrace(toolTrace, exception, startTime);
            publishThinking(toolContext, "博查搜索失败: " + exception.getMessage());
            log.warn("博查搜索失败, query={}", rawQuery, exception);
            throw exception;
        }
    }

    private String resolveFreshness(BochaSearchRequest request) {
        String requested = request != null ? request.getFreshness() : null;
        if (StrUtil.isNotBlank(requested)) {
            String normalized = requested.trim();
            if (ALLOWED_FRESHNESS.contains(normalized)) {
                return normalized;
            }
            log.warn("收到不受支持的博查 freshness: {}, 允许值为 {}", requested, ALLOWED_FRESHNESS);
        }
        return properties.getFreshness();
    }

    @SuppressWarnings("unchecked")
    private void appendReferences(ToolContext toolContext, List<SearchReference> references) {
        RunnableConfig config = ToolContextHelper.getConfig(toolContext).orElse(null);
        if (config == null || references.isEmpty()) {
            return;
        }
        Object container = config.context().get(ChatContextKeys.REFERENCES);
        if (container instanceof List<?> list) {
            ((List<SearchReference>) list).addAll(references);
        }
    }

    @SuppressWarnings("unchecked")
    private void markToolUsed(ToolContext toolContext, String toolName) {
        RunnableConfig config = ToolContextHelper.getConfig(toolContext).orElse(null);
        if (config == null) {
            return;
        }
        Object container = config.context().get(ChatContextKeys.USED_TOOLS);
        if (container instanceof Set<?> set) {
            ((Set<String>) set).add(toolName);
        }
    }

    @SuppressWarnings("unchecked")
    private void publishThinking(ToolContext toolContext, String content) {
        RunnableConfig config = ToolContextHelper.getConfig(toolContext).orElse(null);
        if (config == null) {
            return;
        }
        Object sinkCandidate = config.context().get(ChatContextKeys.EVENT_SINK);
        StreamEventMetadata metadata = resolveMetadata(config);
        if (sinkCandidate instanceof Sinks.Many<?> sink) {
            SinkEmitHelper.emitNext((Sinks.Many<String>) sink, streamEventWriter.thinking(content, metadata));
        }
        Object stepsCandidate = config.context().get(ChatContextKeys.THINKING_STEPS);
        if (stepsCandidate instanceof List<?> list) {
            ((List<String>) list).add(content);
        }
    }

    private StreamEventMetadata resolveMetadata(RunnableConfig config) {
        Object metadataCandidate = config.context().get(ChatContextKeys.EVENT_METADATA);
        if (metadataCandidate instanceof StreamEventMetadata metadata) {
            return metadata;
        }
        return null;
    }

    private ChatToolTrace registerToolTrace(ToolContext toolContext, ChatToolTrace trace) {
        if (trace == null) {
            return trace;
        }
        RunnableConfig config = ToolContextHelper.getConfig(toolContext).orElse(null);
        if (config == null) {
            return trace;
        }
        Object candidate = config.context().get(ChatContextKeys.DEBUG_TRACE);
        if (candidate instanceof ChatDebugTrace debugTrace) {
            debugTrace.getToolTraces().add(trace);
        }
        return trace;
    }

    private void completeToolTrace(ChatToolTrace toolTrace, int referenceCount, long startTime) {
        if (toolTrace == null) {
            return;
        }
        toolTrace.setStatus("COMPLETED");
        toolTrace.setReferenceCount(referenceCount);
        toolTrace.setDurationMs(Math.max(0L, System.currentTimeMillis() - startTime));
        toolTrace.setOutputSummary("博查结果已返回，候选来源 " + referenceCount + " 条");
    }

    private void failToolTrace(ChatToolTrace toolTrace, RuntimeException exception, long startTime) {
        if (toolTrace == null) {
            return;
        }
        toolTrace.setStatus("FAILED");
        toolTrace.setDurationMs(Math.max(0L, System.currentTimeMillis() - startTime));
        toolTrace.setErrorMessage(exception == null ? "" : StrUtil.blankToDefault(exception.getMessage(), ""));
    }

    private record BochaSearchApiRequest(
        String query,
        String freshness,
        boolean summary,
        int count
    ) {
    }

    private record BochaSearchApiResponse(
        Data data
    ) {
        private record Data(
            WebPages webPages
        ) {
        }

        private record WebPages(
            List<BochaResultItem> value
        ) {
        }

        private record BochaResultItem(
            String name,
            String url,
            String snippet,
            String summary
        ) {
        }
    }
}
