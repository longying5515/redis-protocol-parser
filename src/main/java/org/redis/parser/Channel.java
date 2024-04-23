package org.redis.parser;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Channel<T> {
    private BlockingQueue<T> queue = new LinkedBlockingQueue<>();
    private boolean closed = false;

    public void put(T item) throws InterruptedException {
        if (!closed) {
            queue.put(item);
        }
    }

    public T take() throws InterruptedException {
        return queue.take();
    }

    public void close() {
        closed = true;
    }
}

