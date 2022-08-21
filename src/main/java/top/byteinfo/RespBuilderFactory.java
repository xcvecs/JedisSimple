package top.byteinfo;

/**
 * @author ieeiec@outlook.com
 * @Description TODO
 * @createTime 2022-06-28 13:20:00
 */
public class RespBuilderFactory<T> {

    public static final RespBuilder<String> STRING = new RespBuilder<String>() {

        public String build(Object data) {
            return data == null ? null :new String((byte[]) data);
        }

        @Override
        public String toString() {
            return "String";
        }

    };
}
