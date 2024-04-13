package org.redis.reply.customize;

import org.redis.constants.RedisConstants;
import org.redis.reply.Reply;

public class StatusReply implements Reply {
    private String status;

    public StatusReply(String status) {
        this.status = status;
    }

    @Override
    public byte[] toBytes() {
        return ("+" + status + RedisConstants.CRLF).getBytes();
    }
}

