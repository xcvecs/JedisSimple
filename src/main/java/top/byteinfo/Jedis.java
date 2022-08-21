package top.byteinfo;

import top.byteinfo.commands.StringCommands;
import top.byteinfo.net.Connection;
import top.byteinfo.net.DefaultJedisSocketFactory;
import top.byteinfo.net.HostAndPort;
import top.byteinfo.net.JedisSocketFactory;

/**
 * @author ieeiec@outlook.com
 * @Description TODO
 * @createTime 2022-06-22 20:02:00
 */
public class Jedis implements StringCommands {
    private final Connection connection;

    private final CommandBuilder commandBuilder = new CommandBuilder();


    public Jedis(Connection connection) {
        this.connection = connection;
    }

    public Jedis(JedisSocketFactory jedisSocketFactory) {
        this(new Connection(jedisSocketFactory));

    }

    public Jedis(HostAndPort hostAndPort) {
        this(new DefaultJedisSocketFactory(hostAndPort));
    }

    public Jedis(String host, int port) {
        this(new HostAndPort(host, port));
    }


    public Jedis(String url) {
        this(url, 6379);
    }

    public Jedis() {
        this("192.168.1.11");
    }

    //
//    public String set(String key, String value) {
//
//        byte[] keyBytes = key.getBytes();
//        byte[] valueBytes = value.getBytes();
//        List<byte[]> byteList = new ArrayList<>();
//        CommandContent commandContent = new CommandContent(RespBuilderFactory.STRING);
//        byteList.add(commandContent.set());
//        byteList.add(keyBytes);
//        byteList.add(valueBytes);
//
//        return connection.executeCommand(byteList);
//    }
    @Override
    public String set(String key, String value) {

        CommandContent<String> commandContent = new CommandContent<>(RespBuilderFactory.STRING);
        commandContent.set(key, value);
//        return connection.executeCommand(commandContent);
        return connection.executeCommand(commandBuilder.set(key, value));
    }

    @Override
    public String get(String key) {
        return null;
    }

    public Object ping() {
        return "Pong";
    }

//    public String get(String key) {
//
//        byte[] keyBytes = key.getBytes();
//        List<byte[]> byteList = new ArrayList<>();
//        CommandContent commandContent = new CommandContent(RespBuilderFactory.STRING);
//        byteList.add(commandContent.get());
//        byteList.add(keyBytes);
//
//        return connection.executeCommand(byteList);
//    }

}
