package org.javaup.ai.chatagent.support;

/**
 * @description: 支撑组件
 * @author: slienceecheo
 **/

public record StreamEventMetadata(
    String conversationId,
    Long exchangeId
) {
}
