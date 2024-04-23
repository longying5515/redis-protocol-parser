package org.redis.parser;

import org.redis.MyCustomException;
import org.redis.model.Payload;
import org.redis.reply.Reply;
import org.redis.reply.customize.*;
import org.redis.reply.normal.EmptyMultiBulkReply;
import org.redis.reply.normal.NullBulkReply;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class RedisProtocolParser {


    public static Channel<Payload> parseStream(InputStream inputStream) {
        Channel<Payload> channel = new Channel<>(); // Channel是一个自定义的类，模拟Go的channel
        new Thread(() -> {
            try {
                parse0(new InputStreamReader(inputStream), channel);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        return channel;
    }

    public static void parse0(InputStreamReader reader, Channel<Payload> ch) throws InterruptedException {
        try (BufferedReader bufReader = new BufferedReader(reader)) {
            ReadState state = new ReadState();
            String msg;
            while (true) {
                try {
                    msg = bufReader.readLine();
                    if (msg == null) throw new IOException("Stream closed");

                    if (!state.isReadingMultiLine()) {
                        if (msg.startsWith("*")) {
                            handleMultiBulkHeader(msg.getBytes(), state);
                        } else if (msg.startsWith("$")) {
                            parseBulkHeader(msg.getBytes(), state);
                        } else {
                            Reply result = parseSingleLineReply(msg.getBytes());
                            ch.put(new Payload(result, null));
                            state.reset();
                        }
                    } else {
                        readBody(msg.getBytes(), state);
                        if (state.finished()) {
                            Reply result = makeReplyFromState(state);
                            ch.put(new Payload(result, null));
                            state.reset();
                        }
                    }
                } catch (IOException | RuntimeException e) {
                    ch.put(new Payload(null, e));
                    break; // End loop on I/O errors or runtime exceptions
                }
            }
        } catch (Exception e) {
            ch.put(new Payload(null, e));
        } finally {
            ch.close();
        }
    }

    private static Reply makeReplyFromState(ReadState state) {
        if (state.getMsgType() == '*') {
            return new MultiBulkReply(state.getArgs());
        } else if (state.getMsgType() == '$') {
            if (state.getBulkLen() == -1) {
                return NullBulkReply.makeNullBulkReply();
            } else {
                return new BulkReply(state.getArgs()[0]);
            }
        }
        return null;  // 可能需要考虑其他类型的回复或者抛出一个异常
    }

    /**
     * Reads a line from the bufferedReader according to the Redis protocol rules.
     *
     * @param bufferedReader The buffered reader to read from.
     * @param state          Current expected bulk length, or 0 if not in bulk read.
     * @return The line read as a byte array.
     * @throws IOException If there is an I/O error or a protocol error.
     */
    public static byte[] readLine(BufferedReader bufferedReader, ReadState state) throws IOException {
        byte[] msg;

        if (state.getBulkLen() == 0) {
            String line = bufferedReader.readLine();
            if (line == null) {
                throw new IOException("End of stream");
            }
            line += "\r\n";  // Re-adding CRLF for protocol validation
            msg = line.getBytes();

            // Check if the message ends correctly with CRLF (\r\n)
            if (msg.length < 2 || msg[msg.length - 2] != '\r') {
                throw new IOException("Protocol error: Incorrect ending");
            }
        } else {
            msg = new byte[(int) (state.getBulkLen() + 2)];
            String line = bufferedReader.readLine();
            msg = line.getBytes();

            // Ensure the bulk message ends with CRLF
            if (msg.length < 2 || msg[msg.length - 2] != '\r' || msg[msg.length - 1] != '\n') {
                throw new IOException("Protocol error: Incorrect bulk message ending");
            }
        }

        return msg;
    }

    public static void readBody(byte[] msg, ReadState state) {
        // Remove the last two bytes (\r\n)
        byte[] line = new byte[msg.length - 2];
        System.arraycopy(msg, 0, line, 0, line.length);

        try {
            if (line[0] == '$') {
                // Parse the length from the bulk reply header
                String lengthStr = new String(line, 1, line.length - 1, StandardCharsets.UTF_8);
                state.setBulkLen(Integer.parseInt(lengthStr.trim()));

                if (state.getBulkLen() <= 0) { // Handle null bulk reply
                    state.addArg(new byte[0]);
                    state.resetBulkLen();
                }
            } else {
                // It's a normal line, add it to args
                state.addArg(line);
            }
        } catch (NumberFormatException e) {
            throw e;
        }
    }


    private static void handleMultiBulkHeader(byte[] msg, ReadState state) throws InterruptedException, MyCustomException {
        // Multi bulk count
        int count = Integer.parseInt(new String(msg, 1, msg.length - 2));
        if (count == 0) {
            state.setExpectedArgsCount(0);
        } else if (count > 0) {
            state.setMsgType(msg[0]);
            state.setExpectedArgsCount(count);
            state.setReadingMultiLine(true);
        } else {
            throw new MyCustomException("protocol error: " + new String(msg));
        }
    }


    private static void parseBulkHeader(byte[] msg, ReadState state) throws Exception {
        try {
            long bulkLen = Long.parseLong(new String(msg, 1, msg.length - 2));
            if (bulkLen == -1) {
                state.setBulkLen(-1);
            } else if (bulkLen > 0) {
                state.setBulkLen(bulkLen);
                state.setReadingMultiLine(true);
                state.setExpectedArgsCount(1);
            } else {
                throw new Exception("Protocol error: " + msg);
            }
        } catch (NumberFormatException e) {
            throw new Exception("Protocol error: " + msg, e);
        }
    }

    private static Reply parseSingleLineReply(byte[] msg) throws Exception {
        String str = new String(msg).trim();
        switch (msg[0]) {
            case '+': // status reply
                return new StatusReply(str);
            case '-': // error reply
                return new StandardErrReply(str);
            case ':': // integer reply
                try {
                    long val = Long.parseLong(str.substring(1));
                    return new IntReply(val);
                } catch (NumberFormatException e) {
                    throw new Exception("Protocol error: " + str, e);
                }
        }
        return null;
    }

}

