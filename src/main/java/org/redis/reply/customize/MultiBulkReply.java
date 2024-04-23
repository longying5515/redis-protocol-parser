package org.redis.reply.customize;

import org.redis.constants.RedisConstants;
import org.redis.reply.Reply;

import java.util.Arrays;

/**
 *  回复：*3\r\n$3\r\nSET\r\n$3\r\nkey\r\n$5\r\nvalue\r\n
 */
public class MultiBulkReply implements Reply {
    private byte[][] args;

    public MultiBulkReply(byte[][] args) {
        this.args = args;
    }

    @Override
    public byte[] toBytes() {
        StringBuilder builder = new StringBuilder();
        builder.append("*").append(args.length).append(RedisConstants.CRLF);
        Arrays.stream(args).forEach(arg -> {
            if (arg == null) {
                builder.append("$-1").append(RedisConstants.CRLF);
            } else {
                builder.append("$").append(arg.length).append(RedisConstants.CRLF)
                        .append(new String(arg)).append(RedisConstants.CRLF);
            }
        });
        return builder.toString().getBytes();
    }
}

