package com.shushanfx.gson.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by dengjianxin on 2015/11/24.
 */
public class HttpUtils {
    private static final Log logger = LogFactory.getLog(HttpUtils.class);

    public static boolean fetchUrl(String url, String place) {
        boolean ret = true;
        HttpClient client = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(10000)
                .setConnectTimeout(10000)
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);


        httpGet.addHeader("Cache-Control", "max-age=0");
        httpGet.addHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0");
        logger.info("Fetch url: " + url);
        try {
            HttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 400) {
                // 200~400判定为有效url
                File dstFile = new File(place);
                InputStream in = response.getEntity().getContent();
                FileOutputStream out = new FileOutputStream(dstFile);
                byte[] b = new byte[1024];
                int len = 0;
                while ((len = in.read(b)) != -1) {
                    out.write(b, 0, len);
                }
                in.close();
                out.close();
            } else {
                logger.warn("URL -> " + url);
                logger.warn("Error code -> " + statusCode);
                ret = false;
            }
        } catch (IOException e) {
            logger.warn("URL -> " + url, e);
            ret = false;
        } finally {
            httpGet.releaseConnection();
        }

        return ret;
    }
}
