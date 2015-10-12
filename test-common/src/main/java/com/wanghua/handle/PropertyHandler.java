package com.wanghua.handle;

import java.util.Properties;

public final class PropertyHandler
{
    private static Properties props;

    /**
     * 隐藏工具类的构造方法
     */
    protected PropertyHandler()
    {
        throw new UnsupportedOperationException();
    }

    public static void loadProperties(Properties properties)
    {
        props = properties;
    }

    public static String getProperty(String name)
    {
        return props.getProperty(name);
    }
}
