package top.byteinfo.temp;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author ieeiec@outlook.com
 * @Description TODO
 * @createTime 2022-06-24 14:13:00
 */
public class RedisOutputStreamTest extends FilterOutputStream {
    private static final int OUTPUT_BUFFER_SIZE = Integer.parseInt(
            System.getProperty("jedis.bufferSize.output",
                    System.getProperty("jedis.bufferSize", "8192")));

    byte[] buf;
    int count;
    private final static byte[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z' };
    /**
     * Creates an output stream filter built on top of the specified
     * underlying output stream.
     *
     * @param out the underlying output stream to be assigned to
     *            the field <tt>this.out</tt> for later use, or
     *            <code>null</code> if this instance is to be
     *            created without an underlying stream.
     */
    public RedisOutputStreamTest(final OutputStream out) {
        this(out, OUTPUT_BUFFER_SIZE);
    }

    public RedisOutputStreamTest(final OutputStream outputStream, final int size) {
        super(outputStream);
        buf = new byte[size];
        count = 0;
    }

    // len <=9
    public void writeIntCrLf(int len) {
        buf[count++]=digits[len];
        writeCrLf();
    }

    public void writeCrLf() {

        buf[count++] = '\r';
        buf[count++] = '\n';
    }

    public void write(final byte b) throws IOException {

        buf[count++] = b;
    }

    @Override
    public void write(final byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {


        System.arraycopy(b, off, buf, count, len);
        count+=len;


    }
    private void flushBuffer() throws IOException {
        if (count > 0) {
            out.write(buf, 0, count);
            count = 0;
        }
    }
    @Override
    public void flush() throws IOException {
        flushBuffer();
        out.flush();
    }

}
