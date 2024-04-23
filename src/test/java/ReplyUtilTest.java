import org.junit.Test;
import org.redis.reply.ErrorReply;
import org.redis.reply.error.SyntaxErrReply;
import org.redis.utils.ReplyUtil;

public class ReplyUtilTest {
    @Test
    public void test() {
        ErrorReply errorReply=SyntaxErrReply.makeSyntaxErrReply();
        System.out.println(ReplyUtil.isErrorReply(errorReply));
    }
}
