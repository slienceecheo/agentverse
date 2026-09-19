package org.javaup.graphrag.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * @description: 数据实体
 * @author: slienceecheo
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Node("Instructor")
public class Instructor {

    @Id
    private String name;
}
