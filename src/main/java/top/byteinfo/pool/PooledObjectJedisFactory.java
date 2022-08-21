package top.byteinfo.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import top.byteinfo.Jedis;

public class PooledObjectJedisFactory implements PooledObjectFactory<Jedis> {
    @Override
    public void activateObject(PooledObject<Jedis> p) throws Exception {

        final Jedis jedis = p.getObject();

//        if (jedis.getDB() != clientConfig.getDatabase()) {
//            jedis.select(clientConfig.getDatabase());
//        }
    }

    @Override
    public void destroyObject(PooledObject<Jedis> p) throws Exception {
        final Jedis jedis = p.getObject();
//        if (jedis.isConnected()) {
//            try {
//                // need a proper test, probably with mock
//                if (!jedis.isBroken()) {
//                    jedis.quit();
//                }
//            } catch (RuntimeException e) {
//                logger.debug("Error while QUIT", e);
//            }
//            try {
//                jedis.close();
//            } catch (RuntimeException e) {
//                logger.debug("Error while close", e);
//            }
//        }
    }

    @Override
    public PooledObject<Jedis> makeObject() throws Exception {


        Jedis jedis = null;
        jedis = new Jedis("192.168.1.11", 6379);

        return new DefaultPooledObject<>(jedis);

    }

    @Override
    public void passivateObject(PooledObject<Jedis> p) throws Exception {

    }

    @Override
    public boolean validateObject(PooledObject<Jedis> p) {
        final Jedis jedis = p.getObject();
        try {
            // check HostAndPort ??
            return jedis.ping().equals("PONG");
        } catch (final Exception e) {
//            logger.error("Error while validating pooled Jedis object.", e);
            return false;
        }
    }
}
