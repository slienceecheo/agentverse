package org.javaup.ai.manage.service;

/**
 * @description: 服务层
 * @author: slienceecheo
 **/

public interface DocumentTaskLogService {

    void saveLog(Long taskId,
                 Long documentId,
                 Integer stageType,
                 Integer eventType,
                 Integer logLevel,
                 Integer operatorType,
                 Long operatorId,
                 String content,
                 Object detail);
}
