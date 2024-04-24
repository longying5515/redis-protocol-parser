import org.junit.Test;
import org.redis.model.Payload;
import org.redis.parser.ParseStream;
import org.redis.reply.Reply;
import org.redis.reply.error.WrongTypeErrReply;

import java.io.InputStream;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ParseStreamTest {
//    @Test
    public void testParseStream() {
        Payload payload=new Payload();
        Reply wrongTypeReply=new WrongTypeErrReply();
//        InputStream inputStream=wrongTypeReply;
        ParseStream parseStream = new ParseStream();
        FutureTask<Payload> futureTask= new FutureTask<>(parseStream);

    }
}
