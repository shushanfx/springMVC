package com.shushanfx.gson.validate;

/**
 * 校验结果，结果表示如下：<br />
 * code 大于0 表示执行成功 <br />
 * code 等于0 表示未执行 <br />
 * code 小于0 表示执行失败 <br />
 *  Created by dengjianxin on 2015/11/23. <br />
 */
public class ValidateResult {
    public static final int CODE_SUCCESS = 1;
    public static final int CODE_ERROR = -1;
    public static final int CODE_SCHEMA_ERROR = -12;
    public static final int CODE_LENGTH_ERROR = -22;
    public static final int CODE_POOL_ERROR = -31;

    public static final String MESSAGE_SUCCESS = "校验成功";
    public static final String MESSAGE_ERROR = "校验失败";

    private int code = CODE_SUCCESS;
    private String message = "";
    private int schemaCode = CODE_SUCCESS;
    private String schemaMessage = null;
    private int lengthCode = CODE_SUCCESS;
    private String lengthMessage = null;

    public ValidateResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ValidateResult() {
        this(CODE_SUCCESS, MESSAGE_SUCCESS);
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

    public String getSchemaMessage() {
        return schemaMessage;
    }

    public void setSchemaMessage(String schemaMessage) {
        this.schemaMessage = schemaMessage;
    }

    public String getLengthMessage() {
        return lengthMessage;
    }

    public void setLengthMessage(String lengthMessage) {
        this.lengthMessage = lengthMessage;
    }

    public int getSchemaCode() {
        return schemaCode;
    }

    public void setSchemaCode(int schemaCode) {
        this.schemaCode = schemaCode;
    }

    public int getLengthCode() {
        return lengthCode;
    }

    public void setLengthCode(int lengthCode) {
        this.lengthCode = lengthCode;
    }

    public ValidateResult merge(ValidateResult result){
        if(result!=null){
            boolean isError = false;
            if(result.getSchemaCode() < 0){
                this.schemaCode = result.getSchemaCode();
                this.schemaMessage = result.getSchemaMessage();
                isError = true;
            }
            if(result.getLengthCode() < 0){
                this.lengthCode = result.getLengthCode();
                this.lengthMessage = result.getLengthMessage();
                isError = true;
            }
            if(isError && this.getCode() > 0){
                this.code = result.getCode() < 0 ? result.getCode() : CODE_ERROR;
                this.message = result.getCode() < 0 ? result.getMessage() : MESSAGE_ERROR;
            }
        }
        return this;
    }
}
