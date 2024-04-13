package org.redis.utils;

import org.redis.reply.Reply;

public class ReplyUtil {
    public static boolean isErrorReply(Reply reply) {
        return reply.toBytes()[0] == '-';
    }
}

