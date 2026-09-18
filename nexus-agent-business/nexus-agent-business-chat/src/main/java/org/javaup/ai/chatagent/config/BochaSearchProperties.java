package org.javaup.ai.chatagent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 博查（Bocha）联网搜索工具配置。
 * 配置前缀与 Tavily 对齐：app.bocha
 */
@ConfigurationProperties(prefix = "app.bocha")
public class BochaSearchProperties {

    /** 是否启用博查搜索工具；未配置 API Key 时应置为 false。 */
    private boolean enabled = true;

    /** 博查 API 服务地址，可按需切换代理或企业网关。 */
    private String baseUrl = "https://api.bochaai.com";

    /** 博查 Web Search 接口路径。 */
    private String searchPath = "/v1/web-search";

    /** 博查 API Key，推荐通过环境变量注入。 */
    private String apiKey;

    /** 搜索时间范围：noLimit / oneDay / oneWeek / oneMonth / oneYear。 */
    private String freshness = "noLimit";

    /** 是否返回长文本摘要。 */
    private boolean summary = true;

    /** 单次搜索最多返回多少条候选结果。 */
    private int maxResults = 5;

    private int connectTimeoutMs = 3000;

    private int readTimeoutMs = 6000;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getSearchPath() {
        return searchPath;
    }

    public void setSearchPath(String searchPath) {
        this.searchPath = searchPath;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getFreshness() {
        return freshness;
    }

    public void setFreshness(String freshness) {
        this.freshness = freshness;
    }

    public boolean isSummary() {
        return summary;
    }

    public void setSummary(boolean summary) {
        this.summary = summary;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public void setConnectTimeoutMs(int connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public void setReadTimeoutMs(int readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }
}
