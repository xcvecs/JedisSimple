package top.byteinfo.pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import top.byteinfo.Jedis;

public class JedisPool extends GenericObjectPool<Jedis> {

    public JedisPool(PooledObjectFactory<Jedis> factory) {
        super(factory);
    }



}
