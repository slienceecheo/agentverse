package org.javaup.ai.model;

/**
 * @description: 模型对象
 * @author: slienceecheo
 **/
public record RefundResult(
    String orderId,
    boolean accepted,
    String refundNo,
    String message
) {
}
