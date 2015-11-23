package com.shushanfx.gson.validate;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.apache.commons.pool.PoolUtils;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.xml.sax.InputSource;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 校验连接池，初始化后需要对templateDirectory进行设置，<br />
 * 若不初始化templateDirectory，则使用当前目录的template作为初始值。
 * Created by dengjianxin on 2015/11/23.
 */
public final class ValidatePool {
    private ConcurrentHashMap<String, GenericObjectPool> schemaValidation = new ConcurrentHashMap<String, GenericObjectPool>();
    private ConcurrentHashMap<String, GenericObjectPool> lengthValidation = new ConcurrentHashMap<String, GenericObjectPool>();

    private String templateDirectory = "template";
    private int defaultPoolSize = 4;
    private Map<String, Integer> poolSize = null;

    private ValidateResult initPool(String templateNumber) {
        ValidateResult result = new ValidateResult();
        try {
            String key = templateNumber;
            String inputSchemaFile = Paths.get(templateDirectory, "schemaFile" + key + ".xsd").toString();
            String inputLengthFile = Paths.get(templateDirectory, "lengthFile" + key + ".txt").toString();

            int instanceNum = defaultPoolSize;
            if (poolSize != null && poolSize.get(key) != null) {
                instanceNum = poolSize.get(key);
            }

            GenericObjectPool schemapool = new GenericObjectPool(
                    new SchemaValidationObjectFactory(new FileInputStream(inputSchemaFile))
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
        } catch (Exception e) {
            result.setCode(ValidateResult.CODE_POOL_ERROR);
            result.setMessage("模板号[" + templateNumber + "]schema和length文件初始化失败!\r\n" + e.getMessage());
        }
        return result;
    }

    public ValidateResult validate(ValidateAction action) {
        ValidateResult validateResult = new ValidateResult();

        if (schemaValidation.get(action.getTemplateNumber()) == null) {
            ValidateResult temp = initPool(action.getTemplateNumber());
            if (temp.getCode() < 0) {
                // 异常直接返回
                return temp;
            }
        }

        if (action.isSchemaValidate()) {
            validateResult.merge(validateSchema(action));
        }
        if (action.isLengthValidate()) {
            validateResult.merge(validateLength(action));
        }

        return validateResult;
    }

    private ValidateResult validateSchema(ValidateAction action) {
        ValidateResult validateResult = new ValidateResult();
        String key = action.getTemplateNumber();
        InputStreamReader in = null;
        GenericObjectPool pool = schemaValidation.get(key);
        MySAXReader reader = null;
        try {
            in = new InputStreamReader(new FileInputStream(action.getDataFile()), action.getDataFileEncoding());
            InputSource dataFile = new InputSource(in);
            reader = (MySAXReader) pool.borrowObject();
            reader.read(dataFile);

            if (Strings.isNullOrEmpty(reader.getHandleMessage().toString())){
                validateResult.setSchemaCode(ValidateResult.CODE_SUCCESS);
                validateResult.setSchemaMessage("第一步，格式验证成功。");
            } else {
                String message = "第一步，格式验证失败！！！原因如下：\n" + reader.getHandleMessage();
                validateResult.setSchemaCode(ValidateResult.CODE_SCHEMA_ERROR);
                validateResult.setSchemaMessage(message);
            }
        } catch (Exception e) {
            String message = "第一步，格式验证失败！！！发生异常，异常如下：\n" + e.getMessage();
            validateResult.setSchemaCode(ValidateResult.CODE_SCHEMA_ERROR);
            validateResult.setSchemaMessage(message);
        } finally {
            if (reader != null) {
                try {
                    pool.returnObject(reader);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return validateResult;
    }

    private ValidateResult validateLength(ValidateAction action) {
        ValidateResult validateResult = new ValidateResult();

        String key = action.getTemplateNumber();
        InputStreamReader in = null;
        GenericObjectPool pool = schemaValidation.get(key);
        MySAXReader reader = null;
        try {
            in = new InputStreamReader(new FileInputStream(action.getDataFile()), action.getDataFileEncoding());
            InputSource dataFile = new InputSource(in);
            reader = (MySAXReader) pool.borrowObject();
            reader.read(dataFile);

            if (Strings.isNullOrEmpty(reader.getHandleMessage().toString())) {
                validateResult.setLengthCode(ValidateResult.CODE_SUCCESS);
                validateResult.setLengthMessage("第二步，字段长度验证成功。");
            } else {
                String str = "第二步，字段长度验证失败！！！原因如下：\r\n" + reader.getHandleMessage();
                validateResult.setLengthCode(ValidateResult.CODE_LENGTH_ERROR);
                validateResult.setLengthMessage(str);
            }
        } catch (Exception e) {
            String str = "第二步，字段长度验证失败！！！发生异常，异常如下：\r\n" + e.getMessage();
            validateResult.setLengthCode(ValidateResult.CODE_LENGTH_ERROR);
            validateResult.setLengthMessage(str);
        } finally {
            if (reader != null) {
                try {
                    pool.returnObject(reader);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return validateResult;
    }

    public String getTemplateDirectory() {
        return templateDirectory;
    }

    public void setTemplateDirectory(String templateDirectory) {
        this.templateDirectory = templateDirectory;
    }

    public int getDefaultPoolSize() {
        return defaultPoolSize;
    }

    public void setDefaultPoolSize(int defaultPoolSize) {
        this.defaultPoolSize = defaultPoolSize;
    }

    public Map<String, Integer> getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(Map<String, Integer> poolSize) {
        this.poolSize = poolSize;
    }

    public void setPoolSize(String poolSizeString) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        if (poolSizeString != null) {
            String[] arr = poolSizeString.split(" ");
            for (String item : arr) {
                String[] keyValue = item.split("@");
                if (keyValue.length == 2) {
                    try {
                        map.put(keyValue[0].trim(), Integer.parseInt(keyValue[1].trim(), 10));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        this.poolSize = map;
    }

    public static void main(String[] args) {
        String templateDirectory = "/search/odin/resin/xmlview/template";
        String xmlDirectory = "/search/odin/resin/xmlview/xml";

        Gson gson = new Gson();
        ValidatePool pool = new ValidatePool();
        pool.setTemplateDirectory(templateDirectory);
        ValidateAction action1 = new ValidateAction("3028", new File(xmlDirectory, "3028gbk.xml"));
        ValidateResult result = pool.validate(action1);
        System.out.println(gson.toJson(action1));
        System.out.println(gson.toJson(result));

        ValidateAction action2 = new ValidateAction("3028", new File(xmlDirectory, "3028utf8.xml"), "UTF-8");
        result = pool.validate(action2);
        System.out.println(gson.toJson(action2));
        System.out.println(gson.toJson(result));
    }
}
