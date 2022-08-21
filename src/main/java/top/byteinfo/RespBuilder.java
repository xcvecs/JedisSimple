package top.byteinfo;

/**
 * @author ieeiec@outlook.com
 * @Description TODO
 * @createTime 2022-06-28 13:23:00
 */
public abstract class RespBuilder<T> {

    public abstract T build(Object data);
}
