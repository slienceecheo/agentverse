package org.javaup.ai.chatagent.tool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 博查联网搜索工具的入参定义。
 * 与 TavilySearchRequest 保持同构，便于模型在两个搜索工具间无感切换。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BochaSearchRequest {

    private String query;

    /**
     * 搜索时间范围：noLimit / oneDay / oneWeek / oneMonth / oneYear。
     * 缺省由 BochaSearchProperties 兜底。
     */
    private String freshness;

    private Integer maxResults;
}
