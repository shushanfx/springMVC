package com.shushanfx.gson.validate;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import org.apache.commons.codec.binary.StringUtils;

import java.util.Date;

/**
 * Created by dengjianxin on 2015/11/24.
 */
public class ValidateTemplate implements Comparable<ValidateTemplate> {
    private String templateNumber = null;
    private String name = null;
    private String type = null;
    private Date lastModified = null;

    public ValidateTemplate(String name) {
        setName(name);
    }

    public void setName(String name) {
        if(!Strings.isNullOrEmpty(name)){
            templateNumber = name.replaceAll("[^\\d]", "");
            type = Files.getFileExtension(name);
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getTemplateNumber() {
        return templateNumber;
    }

    public String getType() {
        return type;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public int compareTo(ValidateTemplate o) {
        if(Strings.isNullOrEmpty(o.getTemplateNumber()) && Strings.isNullOrEmpty(this.getTemplateNumber())){
            return 0;
        }
        else if(Strings.isNullOrEmpty(o.getTemplateNumber())){
            return 1;
        }
        else if(Strings.isNullOrEmpty(this.getTemplateNumber())){
            return -1;
        }
        else{
            return Integer.parseInt(this.getTemplateNumber(), 10) - Integer.parseInt(o.getTemplateNumber(), 10);
        }
    }
}
