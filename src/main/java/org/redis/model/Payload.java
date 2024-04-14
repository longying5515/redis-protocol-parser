package org.redis.model;


import org.redis.reply.Reply;

public class Payload {
    private Reply data;
    private Exception err;

    public Payload(Reply data, Exception err) {
        this.data = data;
        this.err = err;
    }

    public Reply getData() {
        return data;
    }

    public void setData(Reply data) {
        this.data = data;
    }

    public Exception getErr() {
        return err;
    }

    public void setErr(Exception err) {
        this.err = err;
    }
}
