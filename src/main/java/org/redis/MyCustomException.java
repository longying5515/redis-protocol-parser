package org.redis;

public class MyCustomException extends Exception{
    public MyCustomException() {
        super();
    }

    // 构造器2：接受一个字符串参数，用于指定异常的详细信息
    public MyCustomException(String message) {
        super(message);
    }

    // 构造器3：接受一个字符串参数和一个Throwable类型的参数，用于指定异常的详细信息和原因
    public MyCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    // 构造器4：接受一个Throwable类型的参数，用于指定异常的原因
    public MyCustomException(Throwable cause) {
        super(cause);
    }
}
