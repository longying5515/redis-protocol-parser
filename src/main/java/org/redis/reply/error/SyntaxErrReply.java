package org.redis.reply.error;

import org.redis.reply.ErrorReply;

public class SyntaxErrReply implements ErrorReply {

    private static final byte[] syntaxErrBytes = "-Err syntax error\r\n".getBytes();

    private static final SyntaxErrReply INSTANCE = new SyntaxErrReply();

    // 私有构造函数以防止外部直接实例化
    private SyntaxErrReply() {
    }

    public static SyntaxErrReply makeSyntaxErrReply() {
        return INSTANCE;
    }

    @Override
    public byte[] toBytes() {
        return syntaxErrBytes;
    }

    @Override
    public String getError() {
        return "Err syntax error";
    }
}

