package org.redis.handler;

import org.redis.connection.Connection;
import org.redis.database.Database;
import org.redis.reply.Reply;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class RespHandler {
    private ConcurrentHashMap<Connection, Boolean> activeConn = new ConcurrentHashMap<>();
    private AtomicBoolean closing = new AtomicBoolean(false);
    private Database db;

    public RespHandler(Database db) {
        this.db = db;
    }

    public void close() {
        closing.set(true);
        activeConn.forEach((client, aBoolean) -> {
            try {
                client.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            db.afterClientClose(client);
        });
        db.close();
    }

    public void closeClient(Connection client) throws IOException, InterruptedException {
        client.close();
        db.afterClientClose(client);
        activeConn.remove(client);
    }

    public void handle(Socket socket) throws IOException, InterruptedException {
        if (closing.get()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        Connection client = new Connection(socket);
        activeConn.put(client, Boolean.TRUE);

        try (InputStream inStream = socket.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            String line;
            while ((line = reader.readLine()) != null && !socket.isClosed()) {
                List<byte[]> args = parseArgs(line);
                Reply result = db.exec(client, args);
                client.write(result.toBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeClient(client);
        }
    }

    private List<byte[]> parseArgs(String line) {
        // Parses the incoming string to command arguments
        return Arrays.asList(line.getBytes());
    }
}

