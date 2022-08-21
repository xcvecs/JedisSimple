package top.byteinfo.io;

import top.byteinfo.execeptions.JedisException;

import java.io.*;

/**
 * @author ieeiec@outlook.com
 * @Description TODO
 * @createTime 2022-06-24 14:14:00
 */
public class RedisInputStreamTest extends FilterInputStream {


    protected final byte[] buf;

    protected int count, limit;

    /**
     * Creates a <code>FilterInputStream</code>
     * by assigning the  argument <code>in</code>
     * to the field <code>this.in</code> so as
     * to remember it for later use.
     *
     * @param in the underlying input stream, or <code>null</code> if
     *           this instance is to be created without an underlying stream.
     */
    public RedisInputStreamTest(InputStream in, int size) {
        super(in);
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        buf = new byte[size];

    }

    public RedisInputStreamTest(InputStream in) {
        this(in, 8192);
    }

    public void readContent(byte[] bytes,int len) {

        System.arraycopy(buf, count, bytes, 0,len );

    }

    public byte readByte() {

        ensureFill();
        return buf[count++];
    }

    private void ensureFill() throws JedisException {
        if (count<limit)
            return;
        try {

            limit = in.read(buf);
            count = 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] readLineBytes() {

        int countTemp = this.count;
        final byte[] bufTemp = this.buf;
        while (true) {
            if (bufTemp[countTemp++] == '\r')
                if (bufTemp[countTemp++] == '\n')
                    break;
        }

        int i = countTemp - count-2;


        final byte[] line = new byte[i];
        System.arraycopy(buf, count, line, 0, i);

        count+=i;

        return line;
    }

    public int readIntCrLf() {

        int b = buf[count++]-'0';



        return b;
    }
}
