package org.redis.reply.customize;

import org.redis.constants.RedisConstants;
import org.redis.reply.Reply;

public class IntReply implements Reply {
    private long code;

    public IntReply(long code) {
        this.code = code;
    }

    @Override
    public byte[] toBytes() {
        return (":" + code + RedisConstants.CRLF).getBytes();
    }
}

