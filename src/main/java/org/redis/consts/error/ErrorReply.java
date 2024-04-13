package org.redis.consts.error;

public interface ErrorReply {
    String getError();
    byte[] toBytes();   // 对应于Reply接口的ToBytes()方法
}

