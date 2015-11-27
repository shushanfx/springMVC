package com.shushanfx.gson.validate;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.shushanfx.gson.bean.ResultInfo;
import com.shushanfx.gson.util.HttpUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by dengjianxin on 2015/11/23.
 */
public class ValidateService {
    private ValidatePool validatePool = null;
    private String xmlDirectory = "xml";
    public String getXmlDirectory() {
        return xmlDirectory;
    }

    private Map<String, String> templateMap = null;
    private String templateSuffixString = null;

    public String getTemplateSuffixString() {
        return templateSuffixString;
    }

    public void setTemplateSuffixString(String templateSuffixString) {
        templateMap = new HashMap<String, String>();
        if(Strings.isNullOrEmpty(templateSuffixString)){
            templateSuffixString = "xsl|xsd|txt";
        }
        if(!Strings.isNullOrEmpty(templateSuffixString)){
            String[] arr = templateSuffixString.split("\\|");
            for(String item : arr){
                templateMap.put(item.trim(), item.trim());
            }
        }
        this.templateSuffixString = templateSuffixString;
    }

    public void setXmlDirectory(String xmlDirectory) {
        this.xmlDirectory = xmlDirectory;
    }

    public void setValidatePool(ValidatePool validatePool) {
        this.validatePool = validatePool;
    }

    public ResultInfo validate(String templateID, int type, String xml){
        ResultInfo info = ResultInfo.newSuccess();
        File file = null;
        String charset = "utf-8";
        String fileName = null;
        String content = null;
        boolean isSuccess = true;
        boolean isTemp = false;
        try{
            switch (type){
                case 1:
                    isTemp = true;
                    fileName = "temp" + System.currentTimeMillis() + ".xml";
                    file = new File(xmlDirectory, fileName);
                    Files.write(xml, file, Charset.forName(charset));
                    break;
                case 2:
                    fileName = xml;
                    Path path = Paths.get(xmlDirectory, fileName);
                    file = path.toFile();
                    content = Files.readFirstLine(file, Charset.forName(charset));
                    if(content!=null && (content.indexOf("utf-8")!=-1 || content.indexOf("UTF-8")!= -1)){
                        charset = "utf-8";
                    }
                    else{
                        charset = "gbk";
                    }
                    break;
                case 3:
                    isTemp = true;
                    fileName = "temp" + System.currentTimeMillis() + ".xml";
                    file = new File(xmlDirectory, fileName);
                    isSuccess = HttpUtils.fetchUrl(xml, file.getAbsolutePath());
                    if(isSuccess){
                        content = Files.readFirstLine(file, Charset.forName(charset));
                        if(content!=null && (content.indexOf("utf-8")!=-1 || content.indexOf("UTF-8")!= -1)){
                            charset = "utf-8";
                        }
                        else{
                            charset = "gbk";
                        }
                    }
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(file!=null && isSuccess){
            ValidateAction action = new ValidateAction(templateID, file, charset);
            ValidateResult validateResult = validatePool.validate(action);
            if(validateResult.getCode() < 0){
                info.setCode(ResultInfo.CODE_FAIL);
            }
            info.setMessage(validateResult.getSchemaMessage() + "\n" + validateResult.getLengthMessage());
            if(isTemp){
                file.delete();
            }
        }
        else{
            info.setCode(ResultInfo.CODE_FAIL);
            info.setMessage("获取xml失败！");
        }
        return info;
    }

    public List<ValidateTemplate> listTemplate(final String name){
        List<ValidateTemplate> list = new ArrayList<ValidateTemplate>();

        String schemaDir = validatePool.getTemplateDirectory();
        File file = new File(schemaDir);
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String fileName = pathname.getName();
                if(pathname.isFile() && !Strings.isNullOrEmpty(fileName)){
                    String extName = fileName.substring(fileName.lastIndexOf(".")+1);
                    if(!Strings.isNullOrEmpty(name) && !fileName.contains(name)){
                        return false;
                    }
                    if(templateMap.get(extName)!=null){
                        return true;
                    }
                }
                return false;
            }
        });

        for(File item : files){
            String fileName = item.getName();
            ValidateTemplate template = new ValidateTemplate(fileName);
            template.setLastModified(new Date(item.lastModified()));
            list.add(template);
        }
        if(!list.isEmpty()){
            Collections.sort(list);
        }
        return list;
    }

    public ResultInfo upload(HttpServletRequest request){
        ResultInfo info = ResultInfo.newSuccess();
        boolean needReload = false;
        try {
            DiskFileItemFactory fac = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(fac);
            // 获取多个上传文件
            List fileList = upload.parseRequest(request);
            // 遍历上传文件写入磁盘
            Iterator it = fileList.iterator();
            while (it.hasNext()) {
                Object obit = it.next();
                if (obit instanceof DiskFileItem) {
                    DiskFileItem item = (DiskFileItem) obit;
                    // 如果item是文件上传表单域
                    // 获得文件名及路径
                    String fileName = item.getName();
                    boolean isXml = fileName.contains(".xml");
                    String uploadPath = isXml ? getXmlDirectory() : validatePool.getTemplateDirectory();
                    if (fileName != null) {
                        BufferedInputStream in = new BufferedInputStream(item.getInputStream());// 获得文件输入流
                        BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(FileUtils.getFile(uploadPath, fileName)));// 获得文件输出流
                        Streams.copy(in, outStream, true);// 开始把文件写到你指定的上传文件夹
                        if(!isXml){
                            needReload = true;
                        }
                    }
                }
            }

            if(needReload){
                validatePool.reload();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return info;
    }

}
