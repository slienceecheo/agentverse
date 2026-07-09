package org.javaup.ai.model;

import lombok.Data;

/**
 * @description: 模型对象
 * @author: slienceecheo
 **/
@Data
public class Disease {

    private String id;
    private String name;
    private String symptoms;
    private String treatment;
    private String department;
    private String category;
}
