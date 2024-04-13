package org.redis.reply.customize;

import org.redis.constants.RedisConstants;
import org.redis.reply.ErrorReply;

public class StandardErrReply implements ErrorReply {
    private String status;

    public StandardErrReply(String status) {
        this.status = status;
    }

    @Override
    public byte[] toBytes() {
        return ("-" + status + RedisConstants.CRLF).getBytes();
    }

    @Override
    public String getError() {
        return status;
    }
}

