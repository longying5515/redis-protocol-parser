package org.redis.consts.normal;

import org.redis.Reply;

public class NoReply implements Reply {
    private static final byte[] noBytes = new byte[0];

    @Override
    public byte[] toBytes() {
        return noBytes;
    }
}
