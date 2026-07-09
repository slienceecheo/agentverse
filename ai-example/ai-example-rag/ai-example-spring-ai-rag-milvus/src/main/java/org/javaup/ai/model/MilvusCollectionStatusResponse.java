package org.javaup.ai.model;

import java.util.List;
import java.util.Map;

/**
 * @description: 模型对象
 * @author: slienceecheo
 **/
public record MilvusCollectionStatusResponse(
        String uri,
        String databaseName,
        String collectionName,
        boolean exists,
        String loadState,
        Map<String, String> statistics,
        List<MilvusIndexInfo> indexes,
        String collectionSummary
) {
}
