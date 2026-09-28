package org.javaup.ai.manage.service;

import org.javaup.ai.manage.data.AgentVerseDocumentStructureNode;
import org.javaup.ai.manage.support.DocumentStructureNodeCandidate;

import java.util.List;
import java.util.Map;

/**
 * @description: 服务层
 * @author: slienceecheo
 **/

public interface DocumentStructureNodeService {

    List<AgentVerseDocumentStructureNode> replaceDocumentNodes(Long documentId,
                                                               Long parseTaskId,
                                                               List<DocumentStructureNodeCandidate> candidates);

    List<AgentVerseDocumentStructureNode> listDocumentNodes(Long documentId, Long parseTaskId);

    Map<Long, AgentVerseDocumentStructureNode> nodeMap(Long documentId, Long parseTaskId);

    void deleteByDocumentId(Long documentId);
}
