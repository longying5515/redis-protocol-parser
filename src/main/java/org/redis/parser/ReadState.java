package org.redis.parser;

import lombok.Data;

import java.util.Arrays;

@Data
public class ReadState {
    private boolean readingMultiLine;
    private int expectedArgsCount;
    private byte msgType;
    private byte[][] args;
    private int argCount; // 追踪args中已经添加的元素数量
    private long bulkLen;

    public ReadState() {
        this.args = new byte[10][]; // 初始大小，可以根据需要调整
        this.argCount = 0;
    }

    /**
     * 判断是否已经完成了Redis协议的解析。
     * @return 如果已经读取的参数数量等于预期的参数数量，则返回true，否则返回false。
     */
    public boolean finished() {
        return expectedArgsCount > 0 && argCount == expectedArgsCount;
    }

    /**
     * 向args数组中添加一个新参数。如果数组已满，将其扩容。
     * @param arg 要添加的参数
     */
    public void addArg(byte[] arg) {
        // 检查数组是否已满，如果已满，扩容
        if (argCount >= args.length) {
            increaseArraySize();
        }
        args[argCount++] = arg; // 添加元素并更新计数器
    }

    /**
     * 扩大args数组的大小。
     */
    private void increaseArraySize() {
        int newSize = args.length * 2; // 倍增策略
        byte[][] newArgs = new byte[newSize][];
        System.arraycopy(args, 0, newArgs, 0, args.length);
        args = newArgs;
    }


    public void resetBulkLen() {
        bulkLen = 0;
    }


    public void reset() {
        readingMultiLine = false;
        expectedArgsCount = 0;
        msgType = 0;
        args = new byte[10][];
        argCount = 0;
        bulkLen = 0;
    }

}
