package com.wanghua.exception;

import com.wanghua.constants.DomainType;

/**
 * Created by chenghuanhuan on 15/10/12.
 */
public class BaseException extends Exception{
    protected DomainType domainType = null;

    protected String shortErrorCode = null;

    protected String errorMessage = null;

    protected String errorCode = null;

    public BaseException() {
    }

    public BaseException(DomainType domainType, String shortErrorCode, String errorMessage) {
        super(errorMessage);

        if (domainType == null) {
            return;
        }

        this.shortErrorCode = shortErrorCode;
        this.errorCode = domainType.getDomainType() + shortErrorCode;
        this.errorMessage = errorMessage;
    }

    public BaseException(String errorCode, String errorMessage) {
        super(errorMessage);

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BaseException(BaseException e) {
        super(e != null ? e.getErrorMessage() : "");

        if (e == null) {
            return;
        }

        this.domainType = e.domainType;
        this.shortErrorCode = e.shortErrorCode;
        this.errorCode = e.errorCode;
        this.errorMessage = e.errorMessage;
    }

    public DomainType getDomainType() {
        return domainType;
    }

    public String getShortErrorCode() {
        return shortErrorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
