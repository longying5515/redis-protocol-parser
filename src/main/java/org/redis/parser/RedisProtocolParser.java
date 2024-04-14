package org.redis.parser;
import org.redis.model.Payload;
import org.redis.reply.ErrorReply;
import org.redis.reply.customize.BulkReply;
import org.redis.reply.customize.IntReply;
import org.redis.reply.customize.StandardErrReply;
import org.redis.reply.customize.StatusReply;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class RedisProtocolParser {
    private BufferedReader reader;
    private ReadState state;

    public RedisProtocolParser(BufferedReader reader) {
        this.reader = reader;
        this.state = new ReadState();
    }

    public Payload parse() throws IOException, Exception {
        String line;
        while ((line = reader.readLine()) != null) {
            parseLine(line);
            if (state.finished()) {
                return createPayload();
            }
        }
        return new Payload(null, new Exception("Stream ended unexpectedly"));
    }

    private void parseLine(String line) throws Exception {
        if (state.isReadingMultiLine()) {
            // handle multi-line based on the current state
        } else {
            switch (line.charAt(0)) {
                case '+':
                case '-':
                case ':':
                    parseSingleLineReply(line.getBytes());
                    break;
                case '$':
                    parseBulkHeader(line, state);
                    break;
                // Handle other cases
            }
        }
    }
    public void parse0(BlockingQueue<Payload> queue) {
        ReadState state = new ReadState();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line, state, queue);
                if (state.finished()) {
                    queue.put(new Payload(new BulkReply(line.getBytes()), null));
                    state = new ReadState(); // Reset state for next command
                }
            }
        } catch (IOException | InterruptedException e) {
            try {
                queue.put(new Payload(null, e));
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void parseLine(String line, ReadState state, BlockingQueue<Payload> queue) throws Exception {
        try {
            if (line.startsWith("+")) {
                // Simple String
                handleSimpleString(line, state, queue);
            } else if (line.startsWith("-")) {
                // Error
                handleError(line, state, queue);
            } else if (line.startsWith(":")) {
                // Integer
                handleInteger(line, state, queue);
            } else if (line.startsWith("$")) {
                // Bulk string
                handleBulkString(line, state, queue);
            } else if (line.startsWith("*")) {
                // Multi bulk reply
                handleMultiBulkHeader(line, state, queue);
            }
        } catch (Exception e) {
            queue.put(new Payload(null, e)); // Send error in payload
        }
    }

    private void handleSimpleString(String line, ReadState state, BlockingQueue<Payload> queue) throws InterruptedException {
        // Assuming simple strings are direct replies
        queue.put(new Payload(new StatusReply(line.substring(1)), null));
    }

    private void handleError(String line, ReadState state, BlockingQueue<Payload> queue) throws InterruptedException {
        // Send error message
        queue.put(new Payload(null, new Exception(line.substring(1))));
    }

    private void handleInteger(String line, ReadState state, BlockingQueue<Payload> queue) throws InterruptedException {
        // Process integer
        long number = Long.parseLong(line.substring(1));
        queue.put(new Payload(new IntReply(number), null));
    }

    private void handleBulkString(String line, ReadState state, BlockingQueue<Payload> queue) throws InterruptedException {
        // Bulk string length
        int length = Integer.parseInt(line.substring(1));
        if (length == -1) {
            // Null bulk reply
            queue.put(new Payload(null, null));
        } else {
            // Next line(s) will contain the data
            state.setBulkLen(length);
        }
    }

    private void handleMultiBulkHeader(String line, ReadState state, BlockingQueue<Payload> queue) throws InterruptedException {
        // Multi bulk count
        int count = Integer.parseInt(line.substring(1));
        if (count == -1) {
            // Null multi bulk reply
            queue.put(new Payload(null, null));
        } else {
            state.setExpectedArgsCount(count);
            state.setReadingMultiLine(true);
        }
    }


    private void parseBulkHeader(String msg, ReadState state) throws Exception {
        try {
            long bulkLen = Long.parseLong(msg.substring(1, msg.length() - 2));
            if (bulkLen == -1) {
                state.setBulkLen(-1);
            } else if (bulkLen > 0) {
                state.setBulkLen(bulkLen);
                state.setReadingMultiLine(true);
                state.setExpectedArgsCount(1);
                state.setArgs(new byte[1][]);
            } else {
                throw new Exception("Protocol error: " + msg);
            }
        } catch (NumberFormatException e) {
            throw new Exception("Protocol error: " + msg, e);
        }
    }
    public void parseMultiBulkHeader(String msg, ReadState state) throws Exception {
        try {
            // Strip out the first character and the CRLF at the end before parsing
            long expectedLine = Long.parseLong(msg.substring(1, msg.length() - 2));
            if (expectedLine == 0) {
                state.setExpectedArgsCount(0);
            } else if (expectedLine > 0) {

                state.setMsgType(msg.substring(0,1).getBytes()[0]);
                state.setReadingMultiLine(true);
                state.setExpectedArgsCount((int) expectedLine);
                // Initialize the args list size if necessary
                for (int i = 0; i < expectedLine; i++) {
                    state.addArg(new byte[0]);  // Preallocate space with empty arrays
                }
            } else {
                throw new Exception("Protocol error: Invalid number of lines " + expectedLine);
            }
        } catch (NumberFormatException e) {
            throw new Exception("Protocol error while parsing multi bulk header: " + msg, e);
        }
    }

    private void parseSingleLineReply(byte[] msg) throws Exception {
        String str = new String(msg).trim();
        switch (msg[0]) {
            case '+': // status reply
                state.addArg(new StatusReply(str.substring(1)).toBytes());
                break;
            case '-': // error reply
                state.addArg(new StandardErrReply(str.substring(1)).toBytes());
                break;
            case ':': // integer reply
                try {
                    long val = Long.parseLong(str.substring(1));
                    state.addArg(new IntReply(val).toBytes());
                } catch (NumberFormatException e) {
                    throw new Exception("Protocol error: " + str, e);
                }
                break;
        }
    }

    private Payload createPayload() {
        // Example: assume state stores only one type of reply
        return new Payload(new BulkReply(state.getArgs()[0]), null);
    }
}

