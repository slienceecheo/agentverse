package org.javaup.ai.manage.service;

import org.javaup.ai.manage.data.AgentVerseDocument;
import org.javaup.ai.manage.data.AgentVerseDocumentStrategyPlan;
import org.javaup.ai.manage.data.AgentVerseDocumentStrategyStep;
import org.javaup.ai.manage.support.DocumentAnalysisResult;
import org.javaup.ai.manage.support.DocumentStrategyPlanDraft;
import org.javaup.ai.manage.support.ParentBlockCandidate;

import java.util.List;

/**
 * @description: 服务层
 * @author: slienceecheo
 **/

public interface DocumentStrategyService {

    DocumentStrategyPlanDraft recommendStrategy(AgentVerseDocument document, DocumentAnalysisResult analysisResult);

    List<AgentVerseDocumentStrategyStep> normalizeSteps(AgentVerseDocumentStrategyPlan basePlan,
                                                        List<AgentVerseDocumentStrategyStep> baseSteps,
                                                        List<Integer> requestParentStrategyTypes,
                                                        List<Integer> requestChildStrategyTypes,
                                                        Long documentId);

    List<ParentBlockCandidate> buildParentBlocks(AgentVerseDocument document,
                                                 AgentVerseDocumentStrategyPlan plan,
                                                 List<AgentVerseDocumentStrategyStep> steps,
                                                 String parsedText);
}
