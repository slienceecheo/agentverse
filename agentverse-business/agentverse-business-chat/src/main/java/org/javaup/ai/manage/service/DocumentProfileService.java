package org.javaup.ai.manage.service;

import org.javaup.ai.manage.data.AgentVerseDocumentProfile;
import org.javaup.ai.manage.data.AgentVerseDocumentStructureNode;
import org.javaup.ai.manage.support.DocumentAnalysisResult;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @description: 服务层
 * @author: slienceecheo
 **/
public interface DocumentProfileService {

    AgentVerseDocumentProfile generateProfile(Long documentId,
                                              DocumentAnalysisResult analysisResult,
                                              List<AgentVerseDocumentStructureNode> structureNodes);

    AgentVerseDocumentProfile regenerateProfile(Long documentId);

    List<AgentVerseDocumentProfile> batchRegenerateProfiles(Collection<Long> documentIds);

    Optional<AgentVerseDocumentProfile> getByDocumentId(Long documentId);
}
