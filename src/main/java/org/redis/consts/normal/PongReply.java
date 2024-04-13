package org.redis.consts.normal;

import org.redis.Reply;

public class PongReply implements Reply {
    private static final byte[] pongBytes = "+PONG\r\n".getBytes();

    // Private constructor to enforce the use of factory method
    private PongReply() {
    }

    public static PongReply makePongReply() {
        return new PongReply();
    }

    @Override
    public byte[] toBytes() {
        return pongBytes;
    }
}


