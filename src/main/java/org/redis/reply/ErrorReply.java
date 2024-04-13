package org.redis.reply;

import org.redis.reply.Reply;

public interface ErrorReply extends Reply {
    String getError();
    byte[] toBytes();   // 对应于Reply接口的ToBytes()方法
}

