package com.afirez.knight.core;

import java.util.Objects;

public class AppException extends RuntimeException {
    private int code;
    private String msg;

    public AppException() {

    }

    public AppException(String message) {
        super(message);
        msg = message;
    }

    public AppException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int code() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppException that = (AppException) o;
        return code == that.code &&
                Objects.equals(msg, that.msg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, msg);
    }

    @Override
    public String toString() {
        return "AppException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
