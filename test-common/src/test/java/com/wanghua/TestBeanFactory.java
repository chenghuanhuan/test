package com.wanghua;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * bean工厂
 * Created by chenghuanhuan on 15/10/12.
 */
public class TestBeanFactory {

    private static final ClassPathXmlApplicationContext context;

    protected TestBeanFactory()
    {
        throw new UnsupportedOperationException();
    }

    static
    {
        context = new ClassPathXmlApplicationContext("classpath*:appContext-redis.xml");
    }

    public static <T> T getInstance(Class<T> requiredType)
    {
        return context.getBean(requiredType);
    }

    public static Object getInstance(String name)
    {
        return context.getBean(name);
    }

}
