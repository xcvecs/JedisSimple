package top.byteinfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author ieeiec@outlook.com
 * @Description TODO
 * @createTime 2022-06-24 16:10:00
 */
public class JedisTest {



    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        System.out.println();
        String set = jedis.set("key1", "value1");
//        System.out.println();
        String s = jedis.get("key1");
    }
}
