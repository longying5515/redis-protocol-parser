package org.redis.consts.error.impl;

import org.redis.consts.error.ErrorReply;

public class UnknownErrReply implements ErrorReply {
    private static final byte[] unknownErrBytes = "-Err unknown\r\n".getBytes();
    private static final String errorMessage = "Err unknown";

    @Override
    public byte[] toBytes() {
        return unknownErrBytes;
    }

    @Override
    public String getError() {
        return errorMessage;
    }
}
