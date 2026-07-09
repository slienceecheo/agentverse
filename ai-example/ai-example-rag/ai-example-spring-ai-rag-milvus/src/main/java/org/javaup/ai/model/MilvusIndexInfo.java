package org.javaup.ai.model;

import java.util.Map;

/**
 * @description: 模型对象
 * @author: slienceecheo
 **/
public record MilvusIndexInfo(
        String fieldName,
        String indexName,
        String state,
        long indexedRows,
        long totalRows,
        Map<String, String> params
) {
}
