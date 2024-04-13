package org.redis.reply.error;

import org.redis.reply.ErrorReply;

public class WrongTypeErrReply implements ErrorReply {
    private static final byte[] wrongTypeErrBytes = "-WRONGTYPE Operation against a key holding the wrong kind of value\r\n".getBytes();

    @Override
    public byte[] toBytes() {
        return wrongTypeErrBytes;
    }

    @Override
    public String getError() {
        return "WRONGTYPE Operation against a key holding the wrong kind of value";
    }
}

