package org.javaup.ai.model;

/**
 * @description: 模型对象
 * @author: slienceecheo
 **/
public record OrderInfo(
    String orderId,
    String productName,
    String status,
    String logisticsNo,
    String expectedDelivery,
    boolean canRefund
) {
}
