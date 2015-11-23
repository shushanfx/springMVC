package com.shushanfx.gson.validate;

import java.io.File;

/**
 * Created by dengjianxin on 2015/11/23.
 */
public class ValidateAction {
    private File dataFile = null;
    private String dataFileEncoding = "GBK";
    private String templateNumber = null;

    private boolean isSchemaValidate = true;
    private boolean isLengthValidate = true;

    /**
     * 一个校验动作
     * @param templateNumber 模板号
     * @param dataFile xml文件
     * @param dataFileEncoding xml文件的编码，目前支持gbk和utf-8
     */
    public ValidateAction(String templateNumber, File dataFile, String dataFileEncoding) {
        this.templateNumber = templateNumber;
        this.dataFile = dataFile;
        this.dataFileEncoding = dataFileEncoding;
    }

    /**
     * 默认编码为gbk。
     * @param templateNumber 模板号
     * @param dataFile xml文件
     */
    public ValidateAction(String templateNumber, File dataFile) {
        this(templateNumber, dataFile, "GBK");
    }


    public ValidateResult validate(){
        ValidateResult validateResult = new ValidateResult();
        AladdinValidationUtil aladdinValidationUtil = AladdinValidationUtil.getInstance();



        return validateResult;
    }

    public File getDataFile() {
        return dataFile;
    }

    public void setDataFile(File dataFile) {
        this.dataFile = dataFile;
    }

    public String getDataFileEncoding() {
        return dataFileEncoding;
    }

    public void setDataFileEncoding(String dataFileEncoding) {
        this.dataFileEncoding = dataFileEncoding;
    }

    public String getTemplateNumber() {
        return templateNumber;
    }

    public void setTemplateNumber(String templateNumber) {
        this.templateNumber = templateNumber;
    }

    public boolean isSchemaValidate() {
        return isSchemaValidate;
    }

    public void setSchemaValidate(boolean isSchemaValidate) {
        this.isSchemaValidate = isSchemaValidate;
    }

    public boolean isLengthValidate() {
        return isLengthValidate;
    }

    public void setLengthValidate(boolean isLengthValidate) {
        this.isLengthValidate = isLengthValidate;
    }
}
