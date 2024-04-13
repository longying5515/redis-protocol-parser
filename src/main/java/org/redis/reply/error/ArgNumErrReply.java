package org.redis.reply.error;

import org.redis.reply.ErrorReply;

public class ArgNumErrReply implements ErrorReply {
    private String cmd;

    public ArgNumErrReply(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public byte[] toBytes() {
        String errorMessage = "-ERR wrong number of arguments for '" + this.cmd + "' command\r\n";
        return errorMessage.getBytes();
    }

    @Override
    public String getError() {
        return "ERR wrong number of arguments for '" + this.cmd + "' command";
    }

    public static ArgNumErrReply makeArgNumErrReply(String cmd) {
        return new ArgNumErrReply(cmd);
    }
}

