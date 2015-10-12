package com.wanghua.exception;

import org.apache.commons.lang.StringUtils;

import java.util.Properties;

/**
 * Created by chenghuanhuan on 15/10/12.
 */
public class ErrorHandler {
    private static Properties errorCodesProps;

    /**
     * 隐藏工具类的构造方法
     */
    protected ErrorHandler()
    {
        throw new UnsupportedOperationException();
    }

    public static String getErrMsg(String errId)
    {
        String errMsg = errorCodesProps.getProperty(errId);

        if (StringUtils.isBlank(errMsg))
        {
            errMsg = errId;
        }

        return errMsg;
    }

    public static String getErrMsg(String errId, Object... args)
    {
        String errMsg = errorCodesProps.getProperty(errId);

        if (StringUtils.isBlank(errMsg))
        {
            errMsg = errId;
        }
        else
        {
            errMsg = String.format(errMsg, args);
        }

        return errMsg;
    }

    public static void loadErrorCodes(Properties props)
    {
        errorCodesProps = props;
    }
}
