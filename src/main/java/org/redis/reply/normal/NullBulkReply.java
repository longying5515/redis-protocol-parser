package org.redis.reply.normal;

import org.redis.reply.Reply;

public class NullBulkReply implements Reply {
    private static final byte[] nullBulkBytes = "$-1\r\n".getBytes();

    // 使用私有构造函数来限制外部直接实例化
    private NullBulkReply() {
    }

    // 静态工厂方法提供实例化功能
    public static NullBulkReply makeNullBulkReply() {
        return new NullBulkReply();
    }

    @Override
    public byte[] toBytes() {
        return nullBulkBytes;
    }
}

