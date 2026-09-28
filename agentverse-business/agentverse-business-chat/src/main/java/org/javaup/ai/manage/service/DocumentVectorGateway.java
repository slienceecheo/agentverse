package org.javaup.ai.manage.service;

import org.javaup.ai.manage.data.AgentVerseDocumentChunk;

import java.util.List;

/**
 * @description: 服务层
 * @author: slienceecheo
 **/

public interface DocumentVectorGateway {

    void vectorize(List<AgentVerseDocumentChunk> chunkList);

    void deleteByDocumentId(Long documentId);
}
