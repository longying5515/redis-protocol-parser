package org.redis;

public interface Connection {
    int getDBIndex();
    void selectDB(int dbIndex);
}

