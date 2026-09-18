package org.javaup.ai.chatagent.tool;

import java.util.List;

/**
 * 博查联网搜索工具的返回值。
 * 与 TavilySearchToolResult 同构：查询词 + 归一化引用来源列表。
 */
public record BochaSearchToolResult(
    String query,
    List<SearchReference> references
) {
}
