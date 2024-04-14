package org.redis.connection;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;

public class Connection {
    private final Socket socket;
    private final Lock lock = new ReentrantLock();
    private final Condition operationComplete;
    private int selectDB;

    public Connection(Socket socket) {
        this.socket = socket;
        this.operationComplete = lock.newCondition();
    }

    public void close() throws InterruptedException, IOException {
        lock.lock();
        try {
            if (!operationComplete.await(10, TimeUnit.SECONDS)) {
                System.out.println("Timeout waiting for operations to complete.");
            }
            socket.close();
        } finally {
            lock.unlock();
        }
    }

    public SocketAddress remoteAddr() {
        return socket.getRemoteSocketAddress();
    }

    public void write(byte[] b) throws IOException {
        if (b == null || b.length == 0) {
            return;
        }

        lock.lock();
        try {
            // 模拟开始一个操作
            operationComplete.signal(); // 此处假设只有一个操作需完成
            socket.getOutputStream().write(b);
        } finally {
            lock.unlock();
        }
    }
}

