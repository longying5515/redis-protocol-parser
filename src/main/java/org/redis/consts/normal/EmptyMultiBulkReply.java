package org.redis.consts.normal;

import org.redis.Reply;

public class EmptyMultiBulkReply implements Reply {
    private static final byte[] emptyMultiBulkBytes = "*0\r\n".getBytes();

    // 使用私有构造函数来限制外部直接实例化
    private EmptyMultiBulkReply() {
    }

    // 静态工厂方法提供实例化功能
    public static EmptyMultiBulkReply makeEmptyMultiBulkReply() {
        return new EmptyMultiBulkReply();
    }

    @Override
    public byte[] toBytes() {
        return emptyMultiBulkBytes;
    }
}

