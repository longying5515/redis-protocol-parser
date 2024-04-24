import org.junit.Test;
import org.redis.model.Payload;
import org.redis.parser.Channel;
import org.redis.parser.RedisProtocolParser;
import org.redis.reply.customize.BulkReply;
import org.redis.reply.normal.NullBulkReply;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class RedisProtocolParserTest {
    @Test
    public void testParseStream() throws InterruptedException {
        // 模拟输入流的数据
        String input = "*3\r\n$3\r\nfoo\r\n$-1\r\n$3\r\nbar\r\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        // 调用解析器
        Channel<Payload> channel = RedisProtocolParser.parseStream(inputStream);
        byte[] bytes=channel.take().getData().toBytes();
        System.out.println(new String(bytes));
    }
}

