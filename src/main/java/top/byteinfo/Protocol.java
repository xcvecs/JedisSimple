package top.byteinfo;

import top.byteinfo.execeptions.*;
import top.byteinfo.io.RedisInputStream;
import top.byteinfo.io.RedisOutputStream;
import top.byteinfo.net.HostAndPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author ieeiec@outlook.com
 * @Description TODO
 * @createTime 2022-06-22 20:04:00
 */
public final class Protocol {


    public static final byte DOLLAR_BYTE = '$';
    public static final byte ASTERISK_BYTE = '*';

    public static final byte PLUS_BYTE = '+';

    public static final byte MINUS_BYTE = '-';
    // 这种类型只是一个以 CRLF 结尾的字符串，表示一个整数，前缀为“:”字节。例如，“:0\r\n”和“:1000\r\n”是整数回复。
    public static final byte COLON_BYTE = ':';


    private static final String ASK_PREFIX = "ASK ";
    private static final String MOVED_PREFIX = "MOVED ";
    private static final String CLUSTERDOWN_PREFIX = "CLUSTERDOWN ";
    private static final String BUSY_PREFIX = "BUSY ";
    private static final String NOSCRIPT_PREFIX = "NOSCRIPT ";
    private static final String WRONGPASS_PREFIX = "WRONGPASS";
    private static final String NOPERM_PREFIX = "NOPERM";

    public static volatile Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final OutputStream out;
    private final InputStream in;


    public Protocol(OutputStream out, InputStream in) {
        this.out = out;
        this.in = in;
    }


    public static enum Command {

        PING, SET, GET, GETDEL, GETEX, QUIT, EXISTS, DEL, UNLINK, TYPE, FLUSHDB, KEYS, RANDOMKEY, MOVE,
        RENAME, RENAMENX, DBSIZE, EXPIRE, EXPIREAT, TTL, SELECT, FLUSHALL, GETSET, MGET, SETNX, SETEX,
        MSET, MSETNX, DECRBY, DECR, INCRBY, INCR, APPEND, SUBSTR, HSET, HGET, HSETNX, HMSET, HMGET,
        HINCRBY, HEXISTS, HDEL, HLEN, HKEYS, HVALS, HGETALL, HRANDFIELD, HINCRBYFLOAT, HSTRLEN, MIGRATE,
        RPUSH, LPUSH, LLEN, LRANGE, LTRIM, LINDEX, LSET, LREM, LPOP, RPOP, BLPOP, BRPOP, LINSERT, LPOS,
        RPOPLPUSH, BRPOPLPUSH, BLMOVE, LMOVE, SADD, SMEMBERS, SREM, SPOP, SMOVE, SCARD, SRANDMEMBER,
        SINTER, SINTERSTORE, SUNION, SUNIONSTORE, SDIFF, SDIFFSTORE, SISMEMBER, SMISMEMBER, SINTERCARD,
        MULTI, DISCARD, EXEC, WATCH, UNWATCH, SORT, SORT_RO, AUTH, INFO, SHUTDOWN, MONITOR, CONFIG, LCS,
        SUBSCRIBE, PUBLISH, UNSUBSCRIBE, PSUBSCRIBE, PUNSUBSCRIBE, PUBSUB, STRLEN, LPUSHX, RPUSHX, ECHO,
        ZADD, ZDIFF, ZDIFFSTORE, ZRANGE, ZREM, ZINCRBY, ZRANK, ZREVRANK, ZREVRANGE, ZRANDMEMBER, ZCARD,
        ZSCORE, ZPOPMAX, ZPOPMIN, ZCOUNT, ZUNION, ZUNIONSTORE, ZINTER, ZINTERSTORE, ZRANGEBYSCORE,
        ZREVRANGEBYSCORE, ZREMRANGEBYRANK, ZREMRANGEBYSCORE, ZLEXCOUNT, ZRANGEBYLEX, ZREVRANGEBYLEX,
        ZREMRANGEBYLEX, ZMSCORE, ZRANGESTORE, ZINTERCARD, SAVE, BGSAVE, BGREWRITEAOF, LASTSAVE, PERSIST,
        SETBIT, GETBIT, BITPOS, SETRANGE, GETRANGE, EVAL, EVALSHA, SCRIPT, SLOWLOG, OBJECT, BITCOUNT,
        BITOP, SENTINEL, DUMP, RESTORE, PEXPIRE, PEXPIREAT, PTTL, INCRBYFLOAT, PSETEX, CLIENT, TIME,
        SCAN, HSCAN, SSCAN, ZSCAN, WAIT, CLUSTER, ASKING, READONLY, READWRITE, SLAVEOF, REPLICAOF, COPY,
        PFADD, PFCOUNT, PFMERGE, MODULE, ACL, GEOADD, GEODIST, GEOHASH, GEOPOS, GEORADIUS, GEORADIUS_RO,
        GEORADIUSBYMEMBER, GEORADIUSBYMEMBER_RO, BITFIELD, TOUCH, SWAPDB, MEMORY, BZPOPMIN, BZPOPMAX,
        XADD, XLEN, XDEL, XTRIM, XRANGE, XREVRANGE, XREAD, XACK, XGROUP, XREADGROUP, XPENDING, XCLAIM,
        XAUTOCLAIM, XINFO, BITFIELD_RO, ROLE, FAILOVER, GEOSEARCH, GEOSEARCHSTORE, EVAL_RO, EVALSHA_RO,
        LOLWUT, EXPIRETIME, PEXPIRETIME, FUNCTION, FCALL, FCALL_RO, LMPOP, BLMPOP, ZMPOP, BZMPOP,
        COMMAND, @Deprecated STRALGO;

        private final byte[] raw;

        private Command() {
            raw = name().getBytes(StandardCharsets.UTF_8);

//            raw = SafeEncoder.encode(name());
        }

        //        @Override
        public byte[] getRaw() {
            return raw;
        }
    }

    //    /**
//     * https://redis.io/docs/reference/protocol-spec/
//     * Send commands to a Redis server
//     * As usual, we separate different parts of the protocol with newlines for simplicity,
//     * but the actual interaction is the client sending *2\r\n$4\r\nLLEN\r\n$6\r\nmylist\r\n as a whole
//     *
//     * @param os
//     * @param args
//     */
    public static void sendCommand(RedisOutputStream os, List<byte[]> args) {

        try {
            os.write(ASTERISK_BYTE);
            os.writeIntCrLf(args.size());
            for (byte[] arg : args) {
                os.write(DOLLAR_BYTE);
                os.writeIntCrLf(arg.length);
                os.write(arg);
                os.writeCrLf();
            }
        } catch (IOException e) {
            throw new JedisException(e);
        }
    }

    public static byte[] read(final RedisInputStream is) {

        byte b = is.readByte();
        switch (b) {
            case '+':
                return processStatusCodeReply(is);
            case '$':
                return processBulkReply(is);
            case '-':
                processError(is);
                return null;
            default:
                throw new JedisConnectionException("Unknown reply: " + (char) b);
        }

    }

    private static void processError(final RedisInputStream is) {
        String message = is.readLine();
        // TODO: I'm not sure if this is the best way to do this.
        // Maybe Read only first 5 bytes instead?
        if (message.startsWith(MOVED_PREFIX)) {
            String[] movedInfo = parseTargetHostAndSlot(message);
//      throw new JedisMovedDataException(message, new HostAndPort(movedInfo[1],
//          Integer.parseInt(movedInfo[2])), Integer.parseInt(movedInfo[0]));
            throw new JedisMovedDataException(message, HostAndPort.from(movedInfo[1]), Integer.parseInt(movedInfo[0]));
        } else if (message.startsWith(ASK_PREFIX)) {
            String[] askInfo = parseTargetHostAndSlot(message);
//      throw new JedisAskDataException(message, new HostAndPort(askInfo[1],
//          Integer.parseInt(askInfo[2])), Integer.parseInt(askInfo[0]));
            throw new JedisAskDataException(message, HostAndPort.from(askInfo[1]), Integer.parseInt(askInfo[0]));
        } else if (message.startsWith(CLUSTERDOWN_PREFIX)) {
            throw new JedisClusterException(message);
        } else if (message.startsWith(BUSY_PREFIX)) {
            throw new JedisBusyException(message);
        } else if (message.startsWith(NOSCRIPT_PREFIX)) {
            throw new JedisNoScriptException(message);
        } else if (message.startsWith(WRONGPASS_PREFIX)) {
            throw new JedisAccessControlException(message);
        } else if (message.startsWith(NOPERM_PREFIX)) {
            throw new JedisAccessControlException(message);
        }
        throw new JedisDataException(message);
    }

    private static String[] parseTargetHostAndSlot(String clusterRedirectResponse) {
        String[] response = new String[2];
        String[] messageInfo = clusterRedirectResponse.split(" ");
        response[0] = messageInfo[1];
        response[1] = messageInfo[2];
        return response;
    }

    private static byte[] processBulkReply(RedisInputStream is) {


        final int len = is.readIntCrLf();
        if (len == -1) {
            return null;
        }

        final byte[] read = new byte[len];
        int offset = 0;
        while (offset < len) {
            final int size = is.reads(read, offset, (len - offset));
            if (size == -1) {
                throw new JedisConnectionException("It seems like server has closed the connection.");
            }
            offset += size;
        }

        // read 2 more bytes for the command delimiter
        is.readByte();
        is.readByte();

        return read;
    }

    private static byte[] processStatusCodeReply(RedisInputStream is) {
        return is.readLineBytes();
    }

    public void writeCrLf() throws IOException {
        out.write("\r\n".getBytes());
    }

}
