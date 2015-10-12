package com.wanghua.spring;

import com.wanghua.handle.PropertyHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import java.util.Properties;

/**
 * spring参数获取
 * Created by chenghuanhuan on 15/10/12.
 */
public class SpringPropertyConfigurer extends PropertyPlaceholderConfigurer{
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
            throws BeansException
    {
        logger.info("开始加载properties配置");
        super.processProperties(beanFactory, props);
        PropertyHandler.loadProperties(props);
        logger.info("完成加载properties配置");
    }
}
