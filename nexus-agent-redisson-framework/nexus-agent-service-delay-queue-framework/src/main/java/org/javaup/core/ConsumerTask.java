package org.javaup.core;

/**
 * @description: 延迟队列 消费者接口
 * @author: slienceecheo
 **/
public interface ConsumerTask {

    void execute(String content);

    String topic();
}
