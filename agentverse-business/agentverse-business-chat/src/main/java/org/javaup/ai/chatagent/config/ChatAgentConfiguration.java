package org.javaup.ai.chatagent.config;

import javax.sql.DataSource;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.hook.modelcalllimit.ModelCallLimitHook;
import com.alibaba.cloud.ai.graph.agent.hook.toolcalllimit.ToolCallLimitHook;
import com.alibaba.cloud.ai.graph.agent.interceptor.toolerror.ToolErrorInterceptor;
import com.alibaba.cloud.ai.graph.agent.interceptor.toolretry.ToolRetryInterceptor;
import com.alibaba.cloud.ai.graph.checkpoint.savers.mysql.CreateOption;
import com.alibaba.cloud.ai.graph.checkpoint.savers.mysql.MysqlSaver;
import org.javaup.ai.chatagent.support.DashScopeCompatibilityInterceptor;
import org.javaup.ai.chatagent.support.TavilyToolInputFallbackInterceptor;
import org.javaup.ai.chatagent.tool.BochaSearchRequest;
import org.javaup.ai.chatagent.tool.TavilySearchRequest;
import org.javaup.ai.chatagent.tool.BochaSearchTool;
import org.javaup.ai.chatagent.tool.TavilySearchTool;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 配置类
 * @author: slienceecheo
 **/
@Configuration
@EnableConfigurationProperties({ChatAgentProperties.class, TavilySearchProperties.class, BochaSearchProperties.class})
public class ChatAgentConfiguration {

    @Bean
    public MysqlSaver mysqlCheckpointSaver(DataSource dataSource) {

        return MysqlSaver.builder()
            .dataSource(dataSource)
            .createOption(CreateOption.CREATE_IF_NOT_EXISTS)
            .build();
    }

    @Bean
    public ToolCallback bochaSearchToolCallback(BochaSearchTool bochaSearchTool) {

        return FunctionToolCallback
            .builder("bocha_search", bochaSearchTool::search)
            .description("使用博查搜索引擎联网搜索最新中文信息、事实资料和网页来源。调用时必须传 JSON 参数，且至少包含非空 query；可选 freshness（noLimit/oneDay/oneWeek/oneMonth/oneYear）和 maxResults。")
            .inputType(BochaSearchRequest.class)
            .build();
    }

    @Bean
    public ToolCallback tavilySearchToolCallback(TavilySearchTool tavilySearchTool) {

        return FunctionToolCallback
            .builder("tavily_search", tavilySearchTool::search)
            .description("联网搜索最新信息、事实资料和网页来源。调用时必须传 JSON 参数，且至少包含非空 query；可选 topic 和 maxResults，其中 topic 仅允许 general、news、finance。")
            .inputType(TavilySearchRequest.class)
            .build();
    }

    @Bean
    public ReactAgent businessChatReactAgent(ChatModel chatModel,
                                             MysqlSaver mysqlCheckpointSaver,
                                             ToolCallback tavilySearchToolCallback,
                                             ToolCallback bochaSearchToolCallback,
                                             ChatAgentProperties chatAgentProperties,
                                             DashScopeCompatibilityInterceptor dashScopeCompatibilityInterceptor,
                                             TavilyToolInputFallbackInterceptor tavilyToolInputFallbackInterceptor) {
        return ReactAgent.builder()

            .name("business_chat_agent")
            .model(chatModel)
            .instruction(chatAgentProperties.getSystemPrompt())

            .tools(tavilySearchToolCallback, bochaSearchToolCallback)
            .saver(mysqlCheckpointSaver)

            .parallelToolExecution(true)
            .maxParallelTools(4)

            .hooks(
                ModelCallLimitHook.builder()
                    .runLimit(chatAgentProperties.getMaxModelCallsPerRun())
                    .threadLimit(chatAgentProperties.getMaxModelCallsPerThread())
                    .exitBehavior(ModelCallLimitHook.ExitBehavior.END)
                    .build(),
                ToolCallLimitHook.builder()
                    .toolName("tavily_search")
                    .runLimit(chatAgentProperties.getMaxToolCallsPerRun())
                    .threadLimit(chatAgentProperties.getMaxToolCallsPerThread())
                    .exitBehavior(ToolCallLimitHook.ExitBehavior.END)
                    .build()
            )

            .interceptors(
                dashScopeCompatibilityInterceptor,
                tavilyToolInputFallbackInterceptor,
                ToolRetryInterceptor.builder()
                    .toolName("tavily_search")
                    .maxRetries(2)
                    .initialDelay(200L)
                    .maxDelay(1200L)
                    .jitter(true)
                    .onFailure(ToolRetryInterceptor.OnFailureBehavior.RETURN_MESSAGE)
                    .build(),
                ToolErrorInterceptor.builder().build()
            )
            .build();
    }
}
