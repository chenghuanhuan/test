package com.wanghua.exception;

/**
 * 基础异常定义
 */
public interface BaseError
{

    /**
     * 系统异常
     */
    String SYSTEM_ERROR = "-100001";

    /**
     * 未定义异常
     */
    String NOT_DEFINED_EXCEPTION = "-100002";

    /**
     * 数据库操作异常
     */
    String DATABASE_OPER_EXCEPTION = "-100003";

    /**
     * 参数为空
     */
    String PARAM_EMPTY_ERROR = "-100004";

    /**
     * 数据缓存操作异常
     */
    String CACHE_OPER_EXCEPTION = "-100005";

    /**
     * 参数格式异常
     */
    String PARAM_FORMAT_EXCEPTION = "-100006";

    /**
     * 参数不完整
     */
    String PARAM_INCOMPLETE = "-100007";

    /**
     * 获取redis连接池错误，请稍后再试
     */
    String REDIS_CONN_EXCEPTION = "-100008";
}
