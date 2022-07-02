package top.byteinfo.net;

import top.byteinfo.execeptions.JedisException;

import java.io.IOException;
import java.net.Socket;

/**
 * @author ieeiec@outlook.com
 * @Description TODO
 * @createTime 2022-06-23 22:10:00
 */
public interface JedisSocketFactory {
    Socket createSocket()throws JedisException;

}
