package org.redis.reply.error;

import org.redis.reply.ErrorReply;

public class ProtocolErrReply implements ErrorReply {
    private String msg;

    public ProtocolErrReply(String msg) {
        this.msg = msg;
    }

    @Override
    public byte[] toBytes() {
        String errorMessage = "-ERR Protocol error: '" + this.msg + "'\r\n";
        return errorMessage.getBytes();
    }

    @Override
    public String getError() {
        return "ERR Protocol error '" + this.msg + "' command";
    }
}

