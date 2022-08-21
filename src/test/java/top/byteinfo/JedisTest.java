package top.byteinfo;

import top.byteinfo.pool.JedisPool;
import top.byteinfo.pool.PooledObjectJedisFactory;

/**
 * @author ieeiec@outlook.com
 * @Description TODO
 * @createTime 2022-06-24 16:10:00
 */
public class JedisTest {


    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        System.out.println();
        String set = jedis.set("key1", "value1");
//        System.out.println();
        String s = jedis.get("key1");
    }

  public   static class JedisPoolTest {
        public static void main(String[] args) {
          t1();
        }

        public static void t1() {
            PooledObjectJedisFactory pooledObjectJedisFactory = new PooledObjectJedisFactory();
            JedisPool jedisPool = new JedisPool(pooledObjectJedisFactory);
            try {
                Jedis jedis = jedisPool.borrowObject();
                jedis.set("111", "123");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}
