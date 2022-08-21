package top.byteinfo.net;

import top.byteinfo.Protocol;
import top.byteinfo.execeptions.JedisException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author ieeiec@outlook.com
 * @Description TODO
 * @createTime 2022-06-23 22:11:00
 */
public class DefaultJedisSocketFactory implements JedisSocketFactory {
    private static  HostAndPort hostAndPort;
    //    private int connectionTimeout = Protocol.DEFAULT_TIMEOUT;
    private static int connectionTimeout = 2000;

    public DefaultJedisSocketFactory(HostAndPort hostAndPort) {
        this.hostAndPort=hostAndPort;
    }

    @Override
    public Socket createSocket() throws JedisException {
        String host = hostAndPort.getHost();
        int port = hostAndPort.getPort();
        Socket socket = new Socket();


        try {
            InetAddress inetAddress = InetAddress.getByName(host);

            socket.connect(new InetSocketAddress(inetAddress, port), connectionTimeout);
            return socket;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
