package com.shushanfx.gson.validate;

import com.sun.msv.verifier.jarv.TheFactoryImpl;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.PoolUtils;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.iso_relax.verifier.Schema;
import org.iso_relax.verifier.Verifier;
import org.iso_relax.verifier.VerifierFactory;
import org.iso_relax.verifier.VerifierFilter;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * VR开发dataFile/lengthFile/schemaFile校验工具类
 * User: jikun
 * Date: 2014/8/26
 * Time: 16:02
 */
public class AladdinValidationUtil implements Runnable{
    public static String dataFilePath = "";
    //用于保存总体提示信息
    public static StringBuffer sbTipMessage = new StringBuffer();

    int localInstanceNum ;
    int max;
    String templateNum ;
    String inputDataFile;

    public int getLocalInstanceNum() {
        return localInstanceNum;
    }

    public void setLocalInstanceNum(int localInstanceNum) {
        this.localInstanceNum = localInstanceNum;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getTemplateNum() {
        return templateNum;
    }

    public void setTemplateNum(String templateNum) {
        this.templateNum = templateNum;
    }

    public String getInputDataFile() {
        return inputDataFile;
    }

    public void setInputDataFile(String inputDataFile) {
        this.inputDataFile = inputDataFile;
    }

    CountDownLatch c = null;
    public AladdinValidationUtil(CountDownLatch c) {
        sbTipMessage = new StringBuffer();
        this.c = c;
    }

    public AladdinValidationUtil() {
        sbTipMessage = new StringBuffer();
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            this.validateSchema(this.templateNum);
            this.validateFieldLength(this.templateNum);
        }
        c.countDown();
    }

    public static String validate(String templateNum, HttpServletRequest request, HttpServletResponse response) throws InterruptedException, IOException {
        dataFilePath = getDataFilePath(request);
        AladdinValidationUtil test = new AladdinValidationUtil();
        try {
            // 在一位数模板号前自动补零
            if (templateNum.length() == 1) {
                test.templateNum = "0" + templateNum;
            } else {
                test.templateNum = templateNum;
            }
            String dataFileName = "dataFile" + test.templateNum + ".xml";
            test.inputDataFile = dataFilePath + dataFileName;
            test.initPool(1, Integer.parseInt(test.templateNum), response);
            test.validateSchema(test.templateNum);// 第一步
            AladdinValidationUtil.sbTipMessage.append("\r\n");
            test.validateFieldLength(test.templateNum);// 第二步
            AladdinValidationUtil.sbTipMessage.append("\r\n\r\n");
            return AladdinValidationUtil.sbTipMessage.toString();
        } catch (Exception e) {
            response.getWriter().print("模板编号参数不正确！");
            return "";
        }
    }

    private ConcurrentHashMap<String , GenericObjectPool> schemaValidation = new ConcurrentHashMap<String , GenericObjectPool>();
    private ConcurrentHashMap<String , GenericObjectPool> lengthValidation = new ConcurrentHashMap<String , GenericObjectPool>();
    private static AladdinValidationUtil instance = new AladdinValidationUtil();
    public final static AladdinValidationUtil getInstance() {
        return instance;
    }

    private static String getDataFilePath(HttpServletRequest request) {
        String basePath = request.getSession().getServletContext().getRealPath("");
        return basePath + "templates/";
    }

    public boolean initPool(int instanceNum, int templateNo, HttpServletResponse response) throws IOException {
        try {
            String key = (templateNo<10?"0"+templateNo:String.valueOf(templateNo));
            String inputSchemaFile = dataFilePath + "schemaFile"+key+".xsd";
            String inputLengthFile = dataFilePath + "lengthFile"+key+".txt";

            GenericObjectPool schemapool = new GenericObjectPool(
                    new SchemaObjectFactory(new FileInputStream(inputSchemaFile))
                    , instanceNum,
                    GenericObjectPool.WHEN_EXHAUSTED_BLOCK, -1 // 最大等待时间
                    , -1 // 最大空闲时间
            );
            GenericObjectPool lengthpool = new GenericObjectPool(
                    new LengthValidationObjectFactory(new FileInputStream(inputLengthFile))
                    , instanceNum,
                    GenericObjectPool.WHEN_EXHAUSTED_BLOCK, -1 // 最大等待时间
                    , -1 // 最大空闲时间
            );
            PoolUtils.prefill(schemapool, instanceNum);
            PoolUtils.prefill(lengthpool, instanceNum);
            schemaValidation.put(key, schemapool);
            lengthValidation.put(key, lengthpool);
            return true;
        } catch (Exception e) {
            response.getWriter().print("模板号[" + templateNo + "]schema和length文件初始化失败!\r\n" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean validateSchema(String key) {
        try {
            InputStreamReader dataFile = new InputStreamReader(new FileInputStream(inputDataFile),"GBK");
            return validateSchema(key , dataFile);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 第一步，通过schema验证输入文件的形式
    public boolean validateSchema(String key, Reader in) {
        if (!schemaValidation.containsKey(key))
            return false;
        if (in == null)
            return false;
        GenericObjectPool pool = schemaValidation.get(key);
        MySAXReader reader = null;
        try {
            InputSource dataFile = new InputSource(in);
            reader = (MySAXReader) pool.borrowObject();

			/*
			//1.进行编码验证
			String encoding = new FileCharsetDetector().guestFileEncoding(inputDataFile);
			if( !encoding.startsWith("gb")&&!encoding.startsWith("GB")&&!encoding.startsWith("ASCII") ){
				AladdinValidationUtil.sbTipMessage.append("第零步，文件编码错误。");
				return false;
			}
			//2.进行XML指定编码验证
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputDataFile)));// 接收输入存至缓冲区，参数是System.in
			int c;
			String s = null;
			if ((c = br.read()) != -1) {
				s = br.readLine();
			}
			if( s.indexOf("encoding=\"GBK\"")==-1 && s.indexOf("encoding=\"gbk\"")==-1 )
			{
				AladdinValidationUtil.sbTipMessage.append("第零步，XML文件指定编码错误。");
				return false;
			}*/
            reader.read(dataFile);

            if(reader.getHandleMessage().toString().trim().isEmpty())
                AladdinValidationUtil.sbTipMessage.append("第一步，格式验证成功。");
            else{
                AladdinValidationUtil.sbTipMessage.append("第一步，格式验证失败！！！原因如下：\r\n");
                AladdinValidationUtil.sbTipMessage.append(reader.getHandleMessage());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (reader != null)
                try {
                    pool.returnObject(reader);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return true;
    }

    public boolean validateFieldLength(String key) {
        try {
            InputStreamReader dataFile = new InputStreamReader(new FileInputStream(inputDataFile),"GBK");
            return validateFieldLength(key , dataFile);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 第二步，验证输入文件中每个字段的长度
    public boolean validateFieldLength(String key , Reader in) {
        if (!lengthValidation.containsKey(key))
            return false;
        if (in == null)
            return false;
        GenericObjectPool pool = lengthValidation.get(key);
        MySAXReader reader = null;
        try {
            InputSource dataFile = new InputSource(in);
            reader = (MySAXReader) pool.borrowObject();
            reader.read(dataFile);

            if (reader.getHandleMessage().toString().trim().isEmpty()){
                AladdinValidationUtil.sbTipMessage.append("第二步，字段长度验证成功。");
                return true;
            }
            else {
                AladdinValidationUtil.sbTipMessage.append("第二步，字段长度验证失败！！！原因如下：\r\n");
                AladdinValidationUtil.sbTipMessage.append(reader.getHandleMessage());
                return false;
            }
        } catch (DocumentException e) {
            AladdinValidationUtil.sbTipMessage.append("具体原因如下：\r\n");
            AladdinValidationUtil.sbTipMessage.append(e.getMessage());
            return false;
            // e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (reader != null)
                try {
                    pool.returnObject(reader);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return true;
    }

}

class SchemaObjectFactory extends BasePoolableObjectFactory {

    private byte[] data = null;

    SchemaObjectFactory(InputStream is) throws Exception {
        try {
            int l = is.available();
            this.data = new byte[l];
            is.read(this.data);
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (Exception e) {
                }
        }
    }

    SchemaObjectFactory(byte[] data) throws Exception {
        this.data = data;
    }

    @Override
    public Object makeObject() throws Exception {
        ByteArrayInputStream f = null;
        try {
            MySAXReader reader = new MySAXReader();
            f = new ByteArrayInputStream(this.data);
            VerifierFactory factory = new TheFactoryImpl();
            Schema schema = factory.compileSchema(f);
            Verifier verifier = schema.newVerifier();
            VerifierFilter filter = verifier.getVerifierFilter();
            reader.setXMLFilter(filter);

            MyErrorHandler mehandler = new MyErrorHandler();
            mehandler.setHandleMessage(reader.getHandleMessage());	//把reader的HandleMessage引用传入handler以便写入提示信息
            reader.setErrorHandler(mehandler);
            return reader;
        } catch (Exception e) {
            throw e;
        } finally {
            if (f != null)
                try {
                    f.close();
                } catch (Exception e) {
                }
        }
    }
}


class LengthValidationObjectFactory extends BasePoolableObjectFactory {

    private byte[] data = null;

    LengthValidationObjectFactory(InputStream is) throws Exception {
        try {
            int l = is.available();
            this.data = new byte[l];
            is.read(this.data);
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (Exception e) {
                }
        }
    }

    LengthValidationObjectFactory(byte[] data) throws Exception {
        this.data = data;
    }

    @Override
    public Object makeObject() throws Exception {
        ByteArrayInputStream f = null;
        try {
            f = new ByteArrayInputStream(this.data);
            MySAXReader reader = new MySAXReader();

            //1.读取标签长度配置文件
            List<String> lengthConfigItems = new ArrayList<String>();
            InputStreamReader isr = null;
            try {
                isr = new InputStreamReader(f);
                BufferedReader br = new BufferedReader(isr);
                int c;
                while((c = br.read()) != -1){
                    String s = br.readLine();
                    if(!s.trim().isEmpty())
                        lengthConfigItems.add(s.trim());
                }
            } catch (FileNotFoundException e1) {
                AladdinValidationUtil.sbTipMessage.append("长度检查配置文件不存在！");
            } catch (IOException e1) {
                AladdinValidationUtil.sbTipMessage.append("长度检查配置文件读取出错！");
            }
            //将一个文件中的item进行计数，并在解析完item后释放此节点已减少内存消耗
            countAndRelease(reader);

            CustomElementHandler handler = null;
            String lengthConfigItem = null;
            boolean first = true;
            String tagName;
            int tagMin,tagMax;
            for (int i = 0; i < lengthConfigItems.size(); i++) {
                lengthConfigItem = lengthConfigItems.get(i);
                String[] temp = lengthConfigItem.split("\t");
                tagName = temp[0];
                if(	temp.length == 3 ){//无属性的标签

                    tagMin = Integer.parseInt(temp[1]);
                    tagMax = Integer.parseInt(temp[2]);
                    handler = new CustomElementHandler( tagName, tagMin, tagMax, first);
                    handler.setHandleMessage(reader.getHandleMessage());	//把reader的HandleMessage引用传入handler以便写入提示信息
                    reader.addHandler(handler.getXpath(), handler);
                    first = false;
                }else{//有属性的标签
                    List<AttributeLengthRange> attrlengthConfigs = new ArrayList<AttributeLengthRange>();
                    int attrNum = (temp.length-1) /3;
                    String attr;
                    int mn,mx;
                    for (int j = 0; j < attrNum; j++) {
                        attr = temp[j*3+1];
                        mn = Integer.parseInt( temp[j*3+2] ) ;
                        mx = Integer.parseInt( temp[j*3+3] );
                        attrlengthConfigs.add(new AttributeLengthRange(attr,mn,mx));
                    }
                    handler = new CustomElementHandler( tagName, attrlengthConfigs, first);
                    handler.setHandleMessage(reader.getHandleMessage());	//把reader的HandleMessage引用传入handler以便写入提示信息
                    reader.addHandler(handler.getXpath(), handler);
                }
            }
            return reader;
        } catch (Exception e) {
            throw e;
        } finally {
            if (f != null)
                try {
                    f.close();
                } catch (Exception e) {
                }
        }
    }
    //item计数,释放已处理的item
    void countAndRelease(MySAXReader reader){
        reader.addHandler("/DOCUMENT/item", new ItemHandler(reader.getHandleMessage()));
    }

    class ItemHandler implements ElementHandler {
        private StringBuffer handleMessage = null;
        public ItemHandler(StringBuffer handleMessage) {
            this.handleMessage = handleMessage;
        }
        public void onStart(ElementPath path) {
            //DO Noting
        }
        public void onEnd(ElementPath path) {
            Element row = path.getCurrent();
            if (row.attribute("error") != null) {
                Element temp = row.element("location");
                if (temp != null) {
                    this.handleMessage.append("location: ").append(temp.getStringValue()).append("\r\n");
                }
            }
            row.detach();
        }

    }

}
