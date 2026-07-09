package org.javaup.ai.model;

import java.util.Map;

/**
 * @description: 模型对象
 * @author: slienceecheo
 **/
public record MilvusSearchResult(
        String id,
        String content,
        Double score,
        String docId,
        String category,
        Map<String, Object> metadata
) {
}
