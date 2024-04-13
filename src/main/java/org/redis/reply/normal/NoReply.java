package org.redis.reply.normal;

import org.redis.reply.Reply;

public class NoReply implements Reply {
    private static final byte[] noBytes = new byte[0];

    @Override
    public byte[] toBytes() {
        return noBytes;
    }
}
