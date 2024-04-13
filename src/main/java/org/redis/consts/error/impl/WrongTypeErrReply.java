package org.redis.consts.error.impl;

import org.redis.consts.error.ErrorReply;

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

