package org.redis.reply.normal;

import org.redis.reply.Reply;

public class PongReply implements Reply {
    private static final byte[] pongBytes = "+PONG\r\n".getBytes();


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


