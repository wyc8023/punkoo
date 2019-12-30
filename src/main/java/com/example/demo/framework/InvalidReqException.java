package com.example.demo.framework;

public class InvalidReqException extends Exception {

    private final String userErrMsg;

    private final String errorCode;

    public InvalidReqException( String sysErrMsg) {
        super(sysErrMsg);
        this.userErrMsg = null;
        this.errorCode = "500";
    }

    public String getUserErrMsg() {
        return userErrMsg;
    }

    public String getSysErrMsg() {
        return getMessage();
    }

    public String getErrorCode() {
        return errorCode;
    }
}