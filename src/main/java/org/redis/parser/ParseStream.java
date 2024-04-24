package org.redis.parser;

import org.redis.model.Payload;
import org.redis.reply.customize.BulkReply;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

public class ParseStream implements Callable<Payload> {
    private BufferedReader reader;

    public ParseStream(InputStream inputStream) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public ParseStream() {

    }

    /**
     * 解析出payload结构体
     * @return
     * @throws Exception
     */
    @Override
    public Payload call() throws Exception {
//        RedisProtocolParser parser = new RedisProtocolParser(reader);
        return null;
    }
}
