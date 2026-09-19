package org.javaup.ai.manage.service;

/**
 * @description: 服务层
 * @author: slienceecheo
 **/

public interface DocumentAsyncProcessService {

    void handleParseRoute(Long documentId, Long taskId);

    void handleIndexBuild(Long documentId, Long taskId, Long planId);
}
