package com.shushanfx.gson.validate;

/**
 * User: jikun
 * Date: 2014/8/27
 * Time: 11:07
 */
public class AttributeLengthRange{
    String attributeName;
    int max;
    int min;
    public String getAttributeName() {
        return attributeName;
    }
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public int getMax() {
        return max;
    }
    public void setMax(int max) {
        this.max = max;
    }
    public int getMin() {
        return min;
    }
    public void setMin(int min) {
        this.min = min;
    }

    public AttributeLengthRange(String attributeName, int min,int max) {
        super();
        this.attributeName = attributeName;
        this.max = max;
        this.min = min;
    }
}
