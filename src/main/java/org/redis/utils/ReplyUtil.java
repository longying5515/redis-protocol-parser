package org.redis.utils;

import org.redis.reply.ErrorReply;
import org.redis.reply.Reply;

public class ReplyUtil {
    public static boolean isErrorReply(Reply reply) {
        return reply.toBytes()[0] == '-';
    }
    public static boolean isErrorReply(ErrorReply errorReply) {
        return errorReply.toBytes()[0] == '-';
    }
}

