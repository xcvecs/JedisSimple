package top.byteinfo;

import java.util.List;

/**
 * @author ieeiec@outlook.com
 * @Description TODO
 * @createTime 2022-06-24 13:54:00
 */

public class CommandContentTest<T> {


    private final RespBuilder<T> respBuilder;
    private final List<byte[]> args ;



    public CommandContentTest(List<byte[]> args, RespBuilder<T> respBuilder) {
        this.respBuilder = respBuilder;
        this.args = args;
    }

    public RespBuilder<T> getRespBuilder() {
        return respBuilder;
    }

    public List<byte[]> getArgs() {
        return args;
    }



}
