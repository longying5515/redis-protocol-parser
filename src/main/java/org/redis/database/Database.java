package org.redis.database;

import org.redis.connection.Connection;
import org.redis.reply.Reply;

import java.util.List;

public interface Database {
    Reply exec(Connection client, List<byte[]> args);
    void close();
    void afterClientClose(Connection client);
}
