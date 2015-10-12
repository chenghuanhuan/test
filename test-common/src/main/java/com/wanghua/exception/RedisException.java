package com.wanghua.exception;

/**
 * Created by chenghuanhuan on 15/10/12.
 */
public class RedisException extends BaseException {
    private static final long serialVersionUID = 1612985550064374139L;

    protected String errorId;

    protected String errorMsg;

    public String getErrorId()
    {
        return errorId;
    }

    public void setErrorId(String errorId)
    {
        this.errorId = errorId;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public RedisException()
    {
        super();
    }

    public RedisException(String errId)
    {
        super(errId, ErrorHandler.getErrMsg(errId));
        this.setErrorMsg(ErrorHandler.getErrMsg(errId));
        this.setErrorId(errId);
    }

    public RedisException(Exception e, String errId)
    {
        super(errId, ErrorHandler.getErrMsg(errId));
        this.setErrorId(errId);
        this.setErrorMsg(ErrorHandler.getErrMsg(errId));
    }

    public Throwable fillInStackTrace()
    {
        return null;
    }
}
