package org.javaup.ai.manage.support;

import java.util.List;

/**
 * @description: 支撑组件
 * @author: slienceecheo
 **/

public record DocumentStructureSignalBatch(
    List<String> contextLines,
    List<DocumentStructureSignal> signals
) {
}
