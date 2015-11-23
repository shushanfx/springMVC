package com.shushanfx.gson.validate;

import org.dom4j.io.SAXReader;

/**
 * User: jikun
 * Date: 2014/8/26
 * Time: 16:25
 */
public class MySAXReader extends SAXReader {
    StringBuffer handleMessage = new StringBuffer();

    public StringBuffer getHandleMessage() {
        return handleMessage;
    }

    public void setHandleMessage(StringBuffer handleMessage) {
        this.handleMessage = handleMessage;
    }
}
