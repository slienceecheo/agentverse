package org.javaup.ai.manage.service;

import org.javaup.enums.DocumentFileTypeEnum;
import org.javaup.ai.manage.support.DocumentAnalysisResult;

/**
 * @description: 服务层
 * @author: slienceecheo
 **/

public interface DocumentParserService {

    DocumentAnalysisResult parse(byte[] bytes, String originalFileName, String mimeType, DocumentFileTypeEnum fileType);
}
