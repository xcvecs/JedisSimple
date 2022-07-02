package top.byteinfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ieeiec@outlook.com
 * @Description TODO
 * @createTime 2022-06-28 13:52:00
 */
public class Test {
    public static void main(String[] args) {
        List<byte[]> list =new ArrayList<>();
        CommandContentTest<String> commandContentTest = new CommandContentTest<>(list,RespBuilderFactory.STRING);

        RespBuilder<String> respBuilder = commandContentTest.getRespBuilder();

    }
}
