package org.javaup.ai.model;

/**
 * @description: 模型对象
 * @author: slienceecheo
 **/
public record ProductInfo(
    String productId,
    String productName,
    String price,
    int stock,
    String highlights
) {
}
