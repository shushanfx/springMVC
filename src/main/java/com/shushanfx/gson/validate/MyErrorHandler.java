package com.shushanfx.gson.validate;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * User: jikun
 * Date: 2014/8/27
 * Time: 10:32
 */
public class MyErrorHandler implements ErrorHandler {
    private StringBuffer handleMessage = null;
    public StringBuffer getHandleMessage() {
        return handleMessage;
    }
    public void setHandleMessage(StringBuffer handleMessage) {
        this.handleMessage = handleMessage;
    }
    public void error(SAXParseException e) {
        handleMessage.append("line:").append(e.getLineNumber()).append(",col:").append(e.getColumnNumber()).append(";  ").append(e.getMessage()+"\n");
    }
    public void fatalError(SAXParseException e) {
        handleMessage.append("line:").append(e.getLineNumber()).append(",col:").append(e.getColumnNumber()).append(";  ").append(e.getMessage()+"\n");
    }
    public void warning(SAXParseException e) {
        handleMessage.append("line:").append(e.getLineNumber()).append(",col:").append(e.getColumnNumber()).append(";  ").append(e.getMessage()+"\n");
    }
}
