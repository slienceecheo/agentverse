package org.javaup.ai.manage.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.javaup.database.data.BaseTableData;

import java.math.BigDecimal;

/**
 * @description: 数据实体
 * @author: slienceecheo
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("agentverse_topic_document_relation")
@EqualsAndHashCode(callSuper = true)
public class AgentVerseTopicDocumentRelation extends BaseTableData {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    private String topicCode;

    private Long documentId;

    private BigDecimal relationScore;

    private String relationSource;

    private String reason;
}
