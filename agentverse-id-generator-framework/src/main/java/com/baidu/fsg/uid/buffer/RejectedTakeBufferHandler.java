package com.baidu.fsg.uid.buffer;

/**
 * @description: 处理器
 * @author: slienceecheo
 **/

@FunctionalInterface
public interface RejectedTakeBufferHandler {

    void rejectTakeBuffer(RingBuffer ringBuffer);
}
