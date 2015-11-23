package com.shushanfx.gson.validate;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * User: jikun
 * Date: 2014/8/27
 * Time: 11:06
 */
public class CustomElementHandler implements ElementHandler {

    String xpath;
    int min_bytes,max_bytes;
    boolean isKey;//当前解析元素是否是查询词这个主键
    List<AttributeLengthRange> attrlengthConfigs;
    private StringBuffer handleMessage = null;

    public StringBuffer getHandleMessage() {
        return handleMessage;
    }

    public void setHandleMessage(StringBuffer handleMessage) {
        this.handleMessage = handleMessage;
    }

    public List<AttributeLengthRange> getAttrlengthConfigs() {
        return attrlengthConfigs;
    }

    public void setAttrlengthConfigs(List<AttributeLengthRange> attrlengthConfigs) {
        this.attrlengthConfigs = attrlengthConfigs;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public int getMin_bytes() {
        return min_bytes;
    }

    public void setMin_bytes(int min_bytes) {
        this.min_bytes = min_bytes;
    }

    public int getMax_bytes() {
        return max_bytes;
    }

    public void setMax_bytes(int max_bytes) {
        this.max_bytes = max_bytes;
    }
    public boolean isKey() {
        return isKey;
    }

    public void setKey(boolean isKey) {
        this.isKey = isKey;
    }

    public CustomElementHandler(String xpath, int min_bytes, int max_bytes,boolean isKey) {
        super();
        this.attrlengthConfigs = null;
        this.xpath = xpath;
        this.min_bytes = min_bytes;
        this.max_bytes = max_bytes;
        this.isKey = isKey;
    }
    public CustomElementHandler( String xpath, List<AttributeLengthRange> attrlengthConfigs, boolean isKey){
        super();
        this.xpath = xpath;
        this.attrlengthConfigs = attrlengthConfigs;
        this.isKey = isKey;
    }
    @Override
    public void onEnd(ElementPath path) {
        Element row = path.getCurrent();
        if(attrlengthConfigs == null)//不带属性标签的长度限制
        {
            String value = null;
            if(row.getName().equals("display"))//对display标签中的文本长度做限制2k
                value = row.asXML();
            else
                value = row.getStringValue();

            //判断除display标签以外的字节长度限制【标签】的值两侧是否有特殊字符
            if(!row.getName().equals("display"))
            {
                if(!isKey)
                {
                    if(haveSpecialChars(value)){
                        this.getHandleMessage().append("关键词【"+getKeyOfItem(row)+"】"+xpath.substring(xpath.lastIndexOf('/')+1)+"元素值两侧包含特殊字符：制表符、或换行符、或全半角空格\n");
                    }
                }
                else
                {
                    if(haveSpecialChars(value)){
                        this.getHandleMessage().append("关键词【"+value+"】两侧包含特殊字符：制表符、或换行符、或全半角空格\n");
                    }
                }
            }
            //判断标签值的长度限制
            byte []buff;
            try {
                buff = value.getBytes("GBK");
            } catch (UnsupportedEncodingException e) {
                buff = value.getBytes();
            }
            int value_bytelength = buff.length;
            if( value_bytelength>max_bytes || value_bytelength<min_bytes)
                if(row.getName().equals("display"))//对display标签中的文本长度做限制2k
                    this.getHandleMessage().append("关键词【"+getKeyOfItem(row)+"】的display标签中的文本字节长度为" + value_bytelength + "，不满足长度限制"+min_bytes+"-"+max_bytes+"\n");
                else
                {
                    if(!isKey){
                        this.getHandleMessage().append("关键词【"+getKeyOfItem(row)+"】"+xpath.substring(xpath.lastIndexOf('/')+1)+"元素值字节长度为" + value_bytelength + "，不满足长度限制"+min_bytes+"-"+max_bytes+"\n");
                    }
                    else{
                        this.getHandleMessage().append("关键词【"+value+"】字节长度为" + value_bytelength + "，不满足长度限制"+min_bytes+"-"+max_bytes+"\n");
                    }
                }

        }
        else//带属性标签的长度限制
        {
            //form col的特殊处理
            if(row.getName().matches("^form[0-9]{0,2}$")){
                int min = 0;
                int max = 0;
                String value = "";

                for (int i = 0; i < attrlengthConfigs.size(); i++)
                {
                    AttributeLengthRange alr = attrlengthConfigs.get(i);
                    if (alr.getAttributeName().startsWith("col") && alr.getAttributeName().length() < 6){
                        //col length
                        min += alr.getMin();
                        max += alr.getMax();
                        if (row.attributeValue( alr.getAttributeName() ) != null){
                            value += row.attributeValue( alr.getAttributeName() );
                        }
                    }
                }

                if (min >0 || max > 0){
                    byte[] buff;
                    try {
                        buff = value.getBytes("GBK");
                    } catch (UnsupportedEncodingException e) {
                        buff = value.getBytes();
                    }
                    int value_bytelength = buff.length;
                    if (value_bytelength > max || value_bytelength < min)
                    {
                        this.getHandleMessage().append("关键词【"+ getKeyOfItem(row)+ "】" + xpath.substring(xpath.lastIndexOf('/')+1) + "元素的col属性值总字节长度为" + value_bytelength
                                + "，不满足长度限制" + min + "-"	+ max + "\n");
                    }
                }

            }

            for (int i = 0; i < attrlengthConfigs.size(); i++)
            {
                AttributeLengthRange alr = attrlengthConfigs.get(i);
                if(row.getName().matches("^form[0-9]{0,2}$")){
                    if (alr.getAttributeName().startsWith("col") && alr.getAttributeName().length() < 6){
                        //pass on form col
                        continue;
                    }
                }

                String value="";
                if (row.attributeValue( alr.getAttributeName())!=null)
                {
                    value = row.attributeValue( alr.getAttributeName() );

                    //判断除display标签以外的字节长度限制【属性】的值两侧是否有特殊字符
                    if(haveSpecialChars(value))
                    {
                        this.getHandleMessage().append("关键词【"+ getKeyOfItem(row)+ "】" + xpath.substring(xpath.lastIndexOf('/')+1) + "元素的"
                                + alr.getAttributeName() + "属性值两侧包含特殊字符：制表符、或换行符、或全半角空格\n");
                    }
                    byte[] buff;
                    try {
                        buff = value.getBytes("GBK");
                    } catch (UnsupportedEncodingException e) {
                        buff = value.getBytes();
                    }
                    int value_bytelength = buff.length;
                    if (value_bytelength > alr.getMax() || value_bytelength < alr.getMin())
                    {
                        this.getHandleMessage().append("关键词【"+ getKeyOfItem(row)+ "】" + xpath.substring(xpath.lastIndexOf('/')+1) + "元素的"
                                + alr.getAttributeName() + "属性值字节长度为" + value_bytelength
                                + "，不满足长度限制" + alr.getMin() + "-"
                                + alr.getMax() + "\n");
                    }
                }
            }
            row.detach();
        }
    }
    /**
     * 得到某个row对应的key
     * @param row
     * @return
     */
    private String getKeyOfItem(Element row){
        String ret = "";
        Element temp = getItemElementOfIt(row);
        if(temp != null){
            Element temp1 = temp.element("key");
            if(temp1 != null) {
                ret = temp1.getStringValue();
                if (temp.attribute("error") == null) {
                    temp.addAttribute("error", "1");
                }
            }
        }
        return ret;
    }

    /**
     * 得到包含row的item元素
     * @param row
     * @return
     */
    private Element getItemElementOfIt(Element row){
        if(row == null)
            return null;
        String name = row.getName();
        if(name.equals("item"))
            return row;
        else
            return getItemElementOfIt(row.getParent());
    }

    @Override
    public void onStart(ElementPath path) {
        
    }
    private boolean haveSpecialChars(String input){
        String str = input;
        str = str.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ').replace('　', ' ');
        if(str.length()==str.trim().length())
            return false;
        else
            return true;
    }

}
