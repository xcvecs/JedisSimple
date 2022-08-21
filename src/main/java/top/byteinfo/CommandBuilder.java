package top.byteinfo;


/**
 * @author ieeiec@outlook.com
 * @Description TODO
 * @createTime 2022-06-28 14:07:00
 */
public class CommandBuilder {


    public final CommandContent<String> set(String key, String value) {
        CommandContent<String> commandContent = new CommandContent<>(RespBuilderFactory.STRING);
        commandContent.set(key, value);
        return commandContent;
    }
}
