package com.soybean.test.util;

import org.apache.http.HttpStatus;

public class HttpResult {
    private boolean success;
    private String response;
    private String cookie;
    private String msg;
    private int status;
    private String responseHeader;

    public HttpResult(boolean success, String response, String msg, int status) {
        this.success = success;
        this.response = response;
        this.msg = msg;
        this.status = status;
    }

    public HttpResult(boolean success, String response, String cookie, String msg, int status) {
        this(success, response, msg, status);
        this.cookie = cookie;
    }

    public HttpResult(boolean success, String response, String msg, int status, String responseHeader) {
        this(success, response, msg, status);
        this.responseHeader = responseHeader;
    }

    public HttpResult() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(String responseHeader) {
        this.responseHeader = responseHeader;
    }

    public static HttpResult getFailure(String msg) {
        return new HttpResult(false, null, msg, HttpStatus.SC_BAD_REQUEST);
    }

    public static HttpResult getFailure(String msg, int status) {
        return new HttpResult(false, null, msg, status);
    }

    public static HttpResult getSuccessReturn(String response) {
        return new HttpResult(true, response, null, HttpStatus.SC_OK);
    }

    public static HttpResult getSuccessReturn(String response, String cookie) {
        return new HttpResult(true, response, cookie, null, HttpStatus.SC_OK);
    }

    public static HttpResult getSuccessReturn(String response, String responseHeader, boolean hasHeader) {
        if (hasHeader)
            return new HttpResult(true, response, null, HttpStatus.SC_OK, responseHeader);
        return new HttpResult(true, response, null, HttpStatus.SC_OK);

    }
}
