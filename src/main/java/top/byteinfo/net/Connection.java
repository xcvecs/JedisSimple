package top.byteinfo.net;

import top.byteinfo.CommandContent;
import top.byteinfo.Protocol;
import top.byteinfo.RespBuilder;
import top.byteinfo.execeptions.JedisConnectionException;
import top.byteinfo.io.RedisInputStream;
import top.byteinfo.io.RedisOutputStream;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author ieeiec@outlook.com
 * @Description TODO
 * @createTime 2022-06-23 22:03:00
 */
public class Connection {
    //    private final ArrayList<Rawable> args;
    private Socket socket;
    private final JedisSocketFactory socketFactory;
    private RedisInputStream inputStream;
    private RedisOutputStream outputStream;

    private AtomicBoolean broken = new AtomicBoolean(false);

    public Connection(JedisSocketFactory socketFactory) {
        this.socketFactory = socketFactory;

    }

    public String executeCommand(List<byte[]> byteList) {

        sendCommand(byteList);
        Object one = getOne();

        return (String) one;
    }

    public <T> T executeCommand(final CommandContent<T> commandContent) {
        List<byte[]> args = commandContent.getArgs();
        sendCommand(args);
        return commandContent.getRespBuilder().build(getOne());
    }



    public void sendCommand(List<byte[]> byteList) {
        connect();
        Protocol.sendCommand(outputStream, byteList);
    }


    public boolean isConnected() {
        boolean b = socket != null && socket.isBound() && socket.isConnected() &&
                !socket.isClosed() && !socket.isInputShutdown() && !socket.isOutputShutdown();

        return socket != null && socket.isConnected() && !socket.isClosed() &&
                !socket.isInputShutdown() && !socket.isOutputShutdown();
    }

    protected void flush() {
        try {
            outputStream.flush();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Object getOne() {


        flush();
        try {
            return Protocol.read(inputStream);
        }catch (JedisConnectionException exc) {
            broken.set(true);
            throw exc;
        }


    }

    void connect() {
//        InetAddress;
//        InetSocketAddress;
//        try {
//            InetAddress localHost = InetAddress.getLocalHost();
//            InetAddress inetAddress = InetAddress.getByName("192.168.1.11");
//        } catch (UnknownHostException e) {
//            throw new RuntimeException(e);
//        }
        if (isConnected())
            return;
        socket = socketFactory.createSocket();

        try {
            inputStream = new RedisInputStream(socket.getInputStream());
            outputStream = new RedisOutputStream(socket.getOutputStream());

        } catch (IOException e) {


            throw new RuntimeException(e);
        } finally {
            if (inputStream == null || outputStream == null) {
                try {
                    socket.close();
                } catch (IOException e) {
//                    throw new RuntimeException(e);
                }
            }
        }


    }
}
