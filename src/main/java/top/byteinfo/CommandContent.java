package top.byteinfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ieeiec@outlook.com
 * @Description TODO
 * @createTime 2022-06-24 13:54:00
 */

public class CommandContent<T> {


    private RespBuilder<T> respBuilder;
    private final List<byte[]> args = new ArrayList<>();



    public CommandContent(RespBuilder<T> respBuilder) {
        this.respBuilder = respBuilder;
    }

    public RespBuilder<T> getRespBuilder() {
        return respBuilder;
    }

    public List<byte[]> getArgs() {
        return args;
    }

    public byte[] set() {
        Protocol.Command command = Protocol.Command.SET;
        byte[] bytes = command.getRaw();
        String set = command.name();

        return command.getRaw();
    }

    public byte[] get() {
        Protocol.Command command = Protocol.Command.GET;
        byte[] bytes = command.getRaw();
        String set = command.name();

        return command.getRaw();
    }

    public void set(String key, String value) {
        args.add(Protocol.Command.SET.getRaw());
        args.add(key.getBytes());
        args.add(value.getBytes());

    }

}
