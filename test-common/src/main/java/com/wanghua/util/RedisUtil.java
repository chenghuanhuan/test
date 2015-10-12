package com.wanghua.util;

import com.alibaba.fastjson.JSON;
import com.wanghua.exception.BaseError;
import com.wanghua.exception.RedisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.PipelineBlock;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 商品中心REDIS操作工具类
 */
@Component
public class RedisUtil
{

    @Resource
    private RedisTemplate<Serializable, Serializable> template;

    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    /**
     * 向REDIS里面添加String类型的数据
     * 
     * @author OF708 2013-05-20
     * @param key 类似存储的命名空间
     * @param field 数据key
     * @param value 数据value
     * @throws RedisException
     */
    public long hset(final String key, final String field, final String value) throws RedisException
    {
        if (ParameterUtil.isBlankParams(key, field, value))
        {
            logger.error("入参为空,key={},field={},value={}", key, field, value);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }
        long result = 0;
        try
        {
            result = template.execute(new RedisCallback<Long>()
            {
                @Override
                public Long doInRedis(RedisConnection redisConnection)
                {
                    long res = ((Jedis) redisConnection.getNativeConnection()).hset(key, field, value);
                    logger.debug("向redis里面添加key-value格式的数据 key={},field={},value={},result={}", key, field, value, res);
                    return res;
                }
            });
        }
        catch (Exception e)
        {
            logger.error(" 向REDIS里面添加String类型的数据 异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
        return result;
    }

    /**
     * 从REDIS中读取String类型数据
     * 
     * @author OF708 2013-05-20
     * @param key 类似存储的命名空间
     * @param field 数据key
     * @throws RedisException
     */
    public String hget(final String key, final String field) throws RedisException
    {
        if (ParameterUtil.isBlankParams(key, field))
        {
            logger.error("入参为空,key={},field={}", key, field);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }
        try
        {
            return template.execute(new RedisCallback<String>()
            {
                @Override
                public String doInRedis(RedisConnection connection)
                {
                    return ((Jedis) connection.getNativeConnection()).hget(key, field);
                }
            });
        }
        catch (Exception e)
        {
            logger.error("读取商品中心REDIS数据失败，连接异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
    }

    /**
     * 从redis中读取值为map类型的数据中的某个value
     * 
     * @param key
     * @param field
     * @return Object
     * @throws RedisException
     */
    @SuppressWarnings("rawtypes")
    public Object hgetJSONObject(final String key, final String field, final Class clazz) throws RedisException
    {
        if (ParameterUtil.isBlankParams(key, field) || null == clazz)
        {
            logger.error("入参为空,key={},field={},clazz={}", key, field, clazz);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }

        try
        {
            return template.execute(new RedisCallback<Object>()
            {
                @Override
                public Object doInRedis(RedisConnection redisConnection)
                {
                    String objJson = ((Jedis) redisConnection.getNativeConnection()).hget(key, field);
                    @SuppressWarnings("unchecked")
                    Object obj = JSON.parseObject(objJson, clazz);
                    return obj;
                }
            });
        }
        catch (Exception e)
        {
            logger.error("删除商品中心REDIS数据失败，连接异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
    }

    /**
     * 从redis中设置map类型的数据中的某个value
     * 
     * @param key
     * @param field
     * @return Object
     * @throws RedisException
     */
    public Boolean hsetJSONObject(final String key, final String field, final Object obj) throws RedisException
    {
        if (ParameterUtil.isBlankParams(key, field) || null == obj)
        {
            logger.error("入参为空,key={},field={},obj={}", key, field, obj);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }

        try
        {
            // 写入REDIS
            return template.execute(new RedisCallback<Boolean>()
            {
                @Override
                public Boolean doInRedis(RedisConnection redisConnection)
                {
                    Long result = ((Jedis) redisConnection.getNativeConnection()).hset(key, field,
                        JSON.toJSONString(obj));
                    logger.info("设置redis中信息 key={},field={},obj={},result={}", key, field, obj, result);
                    return 1 == result;
                }
            });
        }
        catch (Exception e)
        {
            logger.error("删除商品中心REDIS数据失败，连接异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
    }

    /**
     * 删除Hashes中的key-field
     * 
     * @author WangJiang May 23, 2013 9:13:52 PM
     * @param key
     * @param field
     * @return
     * @throws RedisException
     */
    public Boolean hdel(final String key, final String field) throws RedisException
    {
        if (ParameterUtil.isBlankParams(key, field))
        {
            logger.error("入参为空,key={},field={}", key, field);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }
        try
        {
            return template.execute(new RedisCallback<Boolean>()
            {
                @Override
                public Boolean doInRedis(RedisConnection redisConnection)
                {
                    Long result = ((Jedis) redisConnection.getNativeConnection()).hdel(key, field);
                    logger.debug("从redis里面删除key-field格式的数据 key={},field={},result={}", key, field, result);
                    return 1 == result;
                }
            });
        }
        catch (Exception e)
        {
            logger.error("删除商品中心REDIS数据失败，连接异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
    }

    /**
     * 判断Hashes中是否存在key-field
     * 
     * @author WangJiang May 23, 2013 9:16:46 PM
     * @param key
     * @param field
     * @return
     * @throws RedisException
     */
    public boolean hexists(final String key, final String field) throws RedisException
    {
        if (ParameterUtil.isBlankParams(key, field))
        {
            logger.error("入参为空,key={},field={}", key, field);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }
        boolean isExists = false;
        try
        {
            isExists = template.execute(new RedisCallback<Boolean>()
            {
                @Override
                public Boolean doInRedis(RedisConnection redisConnection)
                {
                    return ((Jedis) redisConnection.getNativeConnection()).hexists(key, field);
                }
            });
        }
        catch (Exception e)
        {
            logger.error("查询商品中心REDIS数据失败，连接异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
        return isExists;
    }

    /**
     * 批量设置Hashes中的key下的field和value
     * 
     * @author WangJiang May 30, 2013 3:40:01 PM
     * @param key redis中Hashes的key
     * @param fieldValues Map中键作为field，值作为value
     */
    public String hmset(final String key, final Map<String, String> fieldValues) throws RedisException
    {
        if (ParameterUtil.isBlank(key) || null == fieldValues || fieldValues.isEmpty())
        {
            logger.error("入参为空,key={},fieldValues={}", key, fieldValues);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }
        try
        {
            return template.execute(new RedisCallback<String>()
            {
                @Override
                public String doInRedis(RedisConnection redisConnection)
                {
                    return ((Jedis) redisConnection.getNativeConnection()).hmset(key, fieldValues);
                }
            });
        }
        catch (Exception e)
        {
            logger.error("设置商品中心REDIS数据失败，连接异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
    }

    /**
     * 批量设置Hashes中的key下的多个field的value
     * 
     * @author WangJiang May 30, 2013 4:04:05 PM
     * @param key redis中Hashes的key
     * @param fields 要获取的field数组
     * @return
     * @throws RedisException
     */
    public List<String> hmget(final String key, final String... fields) throws RedisException
    {
        if (ParameterUtil.isBlank(key) || ParameterUtil.isBlankParams(fields))
        {
            logger.error("入参为空,key={},fields={}", key, fields);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }
        try
        {
            return template.execute(new RedisCallback<List<String>>()
            {
                @Override
                public List<String> doInRedis(RedisConnection redisConnection)
                {
                    return ((Jedis) redisConnection.getNativeConnection()).hmget(key, fields);
                }
            });
        }
        catch (Exception e)
        {
            logger.error("查询商品中心REDIS数据失败，连接异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
    }

    /**
     * 获取Hashes的一个key下的所有field和value
     * 
     * @author WangJiang May 30, 2013 4:05:20 PM
     * @param key redis中Hashes的key
     * @return
     * @throws RedisException
     */
    public Map<String, String> hgetAll(final String key) throws RedisException
    {
        if (ParameterUtil.isBlank(key))
        {
            logger.error("入参为空,key={}", key);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }
        try
        {
            return template.execute(new RedisCallback<Map<String, String>>()
            {
                @Override
                public Map<String, String> doInRedis(RedisConnection redisConnection)
                {
                    return ((Jedis) redisConnection.getNativeConnection()).hgetAll(key);
                }
            });
        }
        catch (Exception e)
        {
            logger.error("查询商品中心REDIS数据失败，连接异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
    }

    /**
     * 向Sets中添加member
     * 
     * @author WangJiang May 23, 2013 9:18:54 PM
     * @param key
     * @param members 字符串数组作为set中的对象
     * @return
     * @throws RedisException
     */
    public long sadd(final String key, final String... members) throws RedisException
    {
        if (ParameterUtil.isBlank(key) || ParameterUtil.isBlankParams(members))
        {
            logger.error("入参为空,key={},members={}", key, members);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }
        long result = 0;
        try
        {
            result = template.execute(new RedisCallback<Long>()
            {
                @Override
                public Long doInRedis(RedisConnection redisConnection)
                {
                    return ((Jedis) redisConnection.getNativeConnection()).sadd(key, members);
                }
            });
        }
        catch (Exception e)
        {
            logger.error("设置商品中心REDIS数据失败，连接异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
        return result;
    }

    /**
     * 随机返回Sets中的一个member
     * 
     * @author WangJiang May 23, 2013 9:20:18 PM
     * @param key
     * @return
     * @throws RedisException
     */
    public String spop(final String key) throws RedisException
    {
        if (ParameterUtil.isBlank(key))
        {
            logger.error("入参为空,key={}", key);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }
        String result = null;
        try
        {
            result = template.execute(new RedisCallback<String>()
            {
                @Override
                public String doInRedis(RedisConnection redisConnection)
                {
                    return ((Jedis) redisConnection.getNativeConnection()).spop(key);
                }
            });
        }
        catch (Exception e)
        {
            logger.error("读取商品中心REDIS数据失败，连接异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
        return result;
    }

    /**
     * 从Set中删除member
     * 
     * @author WangJiang Sep 6, 2013 5:43:34 PM
     * @param key
     * @param members 字符串数组作为set中的对象
     * @return
     * @throws RedisException
     */
    public long srem(final String key, final String... members) throws RedisException
    {
        if (ParameterUtil.isBlank(key) || ParameterUtil.isBlankParams(members))
        {
            logger.error("入参为空,key={},members={}", key, members);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }
        long result = 0;
        try
        {
            result = template.execute(new RedisCallback<Long>()
            {
                @Override
                public Long doInRedis(RedisConnection redisConnection)
                {
                    return ((Jedis) redisConnection.getNativeConnection()).srem(key, members);
                }
            });
        }
        catch (Exception e)
        {
            logger.error("设置商品中心REDIS数据失败，连接异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
        return result;
    }

    /**
     * 删除传入的keys下的内容，返回删除成功的key的个数
     * 
     * @author WangJiang May 24, 2013 11:15:18 AM
     * @param keys 要删除的key
     * @return
     * @throws RedisException
     */
    public long del(final String... keys) throws RedisException
    {
        if (ParameterUtil.isBlankParams(keys))
        {
            logger.error("入参为空,keys={}", new Object[] { keys });
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }
        long result = 0;
        try
        {
            result = template.execute(new RedisCallback<Long>()
            {
                @Override
                public Long doInRedis(RedisConnection redisConnection)
                {
                    return ((Jedis) redisConnection.getNativeConnection()).del(keys);
                }
            });
        }
        catch (Exception e)
        {
            logger.error("删除传入的keys下的内容异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
        return result;
    }

    /**
     * 判断redis中是否存在指定的key
     * 
     * @author WangJiang May 24, 2013 1:44:35 PM
     * @param key
     * @return
     * @throws RedisException
     */
    public boolean exists(final String key) throws RedisException
    {
        if (ParameterUtil.isBlank(key))
        {
            logger.error("入参为空,key={}", key);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }
        boolean result = false;
        try
        {
            result = template.execute(new RedisCallback<Boolean>()
            {
                @Override
                public Boolean doInRedis(RedisConnection redisConnection)
                {
                    return ((Jedis) redisConnection.getNativeConnection()).exists(key);
                }
            });
        }
        catch (Exception e)
        {
            logger.error("判断redis中是否存在指定的key异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
        return result;
    }

    /**
     * 批量存入数据
     * 
     * @param key key
     * @param map map
     * @return true:操作成功 false:操作失败
     * @throws RedisException 自定义异常
     */
    public boolean pipelined(final String key, final Map<String, String> map) throws RedisException
    {

        if (ParameterUtil.isBlank(key) || null == map || 0 == map.size())
        {
            logger.error("入参为空,key={},map={}", key, map);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }

        try
        {
            return template.execute(new RedisCallback<Boolean>()
            {
                public Boolean doInRedis(final RedisConnection redisConnection)
                {
                    ((Jedis) redisConnection.getNativeConnection()).pipelined(new PipelineBlock()
                    {
                        @Override
                        public void execute()
                        {
                            for (Map.Entry<String, String> entry : map.entrySet())
                            {
                                hset(key, entry.getKey(), entry.getValue());
                            }
                        }
                    });
                    return true;
                }
            });
        }
        catch (Exception e)
        {
            logger.error("批量存入数据异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
    }

    /**
     * 设置key的超时时间，超时后redis会将其删除。注意某些对key的操作会清除超时时间，详细内容参考官方文档。
     * 
     * @author WangJiang Jul 2, 2013 11:14:25 AM
     * @param key key
     * @param seconds 超时时间，单位秒
     * @return
     * @throws RedisException
     */
    public boolean expire(final String key, final int seconds) throws RedisException
    {
        if (ParameterUtil.isBlank(key) || 0 > seconds)
        {
            logger.error("入参有误,key={},seconds={}", key, seconds);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }
        try
        {
            return template.execute(new RedisCallback<Boolean>()
            {
                public Boolean doInRedis(final RedisConnection redisConnection)
                {
                    Long result = ((Jedis) redisConnection.getNativeConnection()).expire(key, seconds);
                    return 1 == result;
                }
            });
        }
        catch (Exception e)
        {
            logger.error("设置商品中心REDIS数据失败，连接异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
    }

    /**
     * 向redis中添加json字符串
     * 
     * @author of518
     * @param key
     * @return Object
     * @throws RedisException
     */
    public Boolean setJSONObject(final String key, final Object obj) throws RedisException
    {
        if (ParameterUtil.isBlankParams(key) || null == obj)
        {
            logger.error("入参为空,key={},obj={}", key, obj);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }
        // 写入REDIS
        try
        {
            return template.execute(new RedisCallback<Boolean>()
            {
                @Override
                public Boolean doInRedis(RedisConnection redisConnection)
                {
                    String result = ((Jedis) redisConnection.getNativeConnection()).set(key, JSON.toJSONString(obj));
                    logger.info("设置redis中信息 key={},obj={},result={}", key, obj, result);
                    return "OK".equals(result);
                }
            });
        }
        catch (Exception e)
        {
            logger.error("向redis中添加json字符串异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
    }

    /**
     * 从redis中读取json字符串
     * 
     * @author of518
     * @param key
     * @throws RedisException
     */
    public String get(final String key) throws RedisException
    {
        if (ParameterUtil.isBlankParams(key))
        {
            logger.error("入参为空,key={}", key);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }

        try
        {
            return template.execute(new RedisCallback<String>()
            {
                @Override
                public String doInRedis(RedisConnection redisConnection)
                {
                    return ((Jedis) redisConnection.getNativeConnection()).get(key);
                }
            });
        }
        catch (Exception e)
        {
            logger.error("从redis中读取json字符串异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
    }
    
    /**
     * 从redis中设置json字符串
     * 
     * @author of518
     * @param key
     * @param value
     * @throws RedisException
     */
    public String set(final String key,final String value) throws RedisException
    {
        if (ParameterUtil.isBlankParams(key,value))
        {
            logger.error("入参为空,key={},value={}", key,value);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }

        try
        {
            return template.execute(new RedisCallback<String>()
            {
                @Override
                public String doInRedis(RedisConnection redisConnection)
                {
                    return ((Jedis) redisConnection.getNativeConnection()).set(key,value);
                }
            });
        }
        catch (Exception e)
        {
            logger.error("从redis中设置json字符串异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
    }

    /**
     * 使用管道的sadd
     * 
     * @author WangJiang Sep 3, 2013 10:15:15 AM
     * @param key
     * @param values
     * @return
     * @throws RedisException
     */
    public boolean pipelinedSadd(final String key, final List<String> values) throws RedisException
    {
        if (ParameterUtil.isBlank(key) || null == values || 0 == values.size())
        {
            logger.error("入参为空,key={},values={}", key, values);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }

        try
        {
            return template.execute(new RedisCallback<Boolean>()
            {
                public Boolean doInRedis(final RedisConnection redisConnection)
                {
                    ((Jedis) redisConnection.getNativeConnection()).pipelined(new PipelineBlock()
                    {

                        @Override
                        public void execute()
                        {
                            for (String value : values)
                            {
                                sadd(key, value);
                            }
                        }
                    });
                    return true;
                }
            });
        }
        catch (Exception e)
        {
            logger.error("使用管道的sadd异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
    }

    /**
     * 使用管道的srem
     * 
     * @author WangJiang Sep 3, 2013 10:15:31 AM
     * @param key
     * @param values
     * @return
     * @throws RedisException
     */
    public boolean pipelinedSrem(final String key, final List<String> values) throws RedisException
    {
        if (ParameterUtil.isBlank(key) || null == values || 0 == values.size())
        {
            logger.error("入参为空,key={},values={}", key, values);
            throw new RedisException(BaseError.PARAM_EMPTY_ERROR);
        }

        try
        {
            return template.execute(new RedisCallback<Boolean>()
            {
                public Boolean doInRedis(final RedisConnection redisConnection)
                {
                    ((Jedis) redisConnection.getNativeConnection()).pipelined(new PipelineBlock()
                    {

                        @Override
                        public void execute()
                        {
                            for (String value : values)
                            {
                                srem(key, value);
                            }
                        }
                    });

                    return true;
                }
            });
        }
        catch (Exception e)
        {
            logger.error("使用管道的srem异常，请检查相关配置。错误：{}", e.getMessage());
            throw new RedisException(e, BaseError.REDIS_CONN_EXCEPTION);
        }
    }
}
