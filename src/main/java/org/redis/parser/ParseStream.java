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

    @Override
    public Payload call() throws Exception {
        ReadState state = new ReadState();
        String line;
        while ((line = reader.readLine()) != null) {
            // Assume readLine and parse logic is correctly implemented
            byte[] msg = line.getBytes();
            // Dummy example of handling parsed line
            if (state.getExpectedArgsCount() > 0 && state.getArgs().length == state.getExpectedArgsCount()) {
                return new Payload(new BulkReply(msg), null);
            }
        }
        return new Payload(null, new Exception("Stream ended unexpectedly"));
    }
}
