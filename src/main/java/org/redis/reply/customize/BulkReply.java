package org.redis.reply.customize;

import org.redis.constants.RedisConstants;
import org.redis.reply.Reply;

public class BulkReply implements Reply {
    private byte[] arg;

    public BulkReply(byte[] arg) {
        this.arg = arg;
    }

    @Override
    public byte[] toBytes() {
        if (arg == null) {
            return "$-1\r\n".getBytes();
        }
        return ("$" + arg.length + RedisConstants.CRLF + new String(arg) + RedisConstants.CRLF).getBytes();
    }
}

