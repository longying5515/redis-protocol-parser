import org.junit.Test;
import org.redis.reply.customize.MultiBulkReply;

public class MultiBulkTest {
    @Test
    public void testMultiBulk() {
        String[] strings={"good","eat","diff"};
        byte[][] bytes=new byte[strings.length][];
        for(int i=0;i<strings.length;i++){
            bytes[i]=strings[i].getBytes();
        }
        MultiBulkReply reply=new MultiBulkReply(bytes);
        System.out.println(new String(reply.toBytes()));
    }
}
