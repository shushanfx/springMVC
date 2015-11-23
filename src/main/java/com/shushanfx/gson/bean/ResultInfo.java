package com.shushanfx.gson.bean;

/**
 * Created by dengjianxin on 2015/11/23.
 */
public class ResultInfo {
    public static final int CODE_SUCCESS = 1;
    public static final int CODE_FAIL = -1;

    private int code = CODE_SUCCESS;
    private String message = null;
    private Object data = null;

    public ResultInfo(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultInfo(int code, String message){
        this(code, message, null);
    }

    public ResultInfo(){
        this(CODE_SUCCESS, "操作成功", null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
