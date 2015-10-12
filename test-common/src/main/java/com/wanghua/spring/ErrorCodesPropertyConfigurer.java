package com.wanghua.spring;

import com.wanghua.exception.ErrorHandler;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * 加载异常码
 * Created by chenghuanhuan on 15/10/12.
 */
public class ErrorCodesPropertyConfigurer extends PropertyPlaceholderConfigurer{
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
            throws BeansException
    {
        super.processProperties(beanFactory, props);
        ErrorHandler.loadErrorCodes(props);
        ConvertUtils.register(new DateConverter(), java.util.Date.class);
        ConvertUtils.register(new DateConverter(), java.sql.Date.class);
        ConvertUtils.register(new DateConverter(), java.sql.Timestamp.class);
        ConvertUtils.register(null, java.math.BigDecimal.class);
        ConvertUtils.register(null, java.lang.Short.class);
        ConvertUtils.register(null, java.lang.Integer.class);
        ConvertUtils.register(null, java.lang.Double.class);
        ConvertUtils.register(null, java.lang.Float.class);
        ConvertUtils.register(null, java.lang.Short.class);
        ConvertUtils.register(null, java.lang.Long.class);
    }
}
