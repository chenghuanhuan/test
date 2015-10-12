package com.wanghua;

import com.wanghua.exception.RedisException;
import com.wanghua.util.RedisUtil;

/**
 * Created by chenghuanhuan on 15/10/12.
 */
public class RedisUtilTest {
    public static void main(String[] args)
    {
        RedisUtil redisUtil = TestBeanFactory.getInstance(RedisUtil.class);
        System.out.println("-------------");
        System.out.println(redisUtil);

        try {
            long l = redisUtil.hset("test","a","b");
            String s =redisUtil.hget("test","a");
            System.out.println(s);
        } catch (RedisException e) {
            e.printStackTrace();
        }
    }
}
