package org.redis.consts.normal;

import org.redis.Reply;

public class OkReply implements Reply {
    private static final byte[] okBytes = "+OK\r\n".getBytes();

    // 使用单例模式，因为每次都是相同的响应
    private static final OkReply INSTANCE = new OkReply();

    // 私有构造函数防止外部直接实例化
    private OkReply() {
    }

    public static OkReply makeOkReply() {
        return INSTANCE;
    }

    @Override
    public byte[] toBytes() {
        return okBytes;
    }
}

