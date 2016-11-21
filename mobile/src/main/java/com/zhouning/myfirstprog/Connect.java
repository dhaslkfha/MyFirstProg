package com.zhouning.myfirstprog;


import com.zhouning.myfirstprog.tool.Logout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 宁 on 2016/11/8.
 * 用于Http网络请求，使用时先获取实例
 * @see Connect#getInstance()，再调用方法
 * @see Connect#connect(String, String, String) ;
 */
public class Connect {
    static Connect connect;

    public static Connect getInstance() {
        if (connect == null) {
            connect = new Connect();
        }
        return connect;
    }

    /**
     * HTTP返回结果
     */
    String result = "";

    /**
     * 下面为HTTP的请求方式
     */
    public String connect(String data, String requestCode, String versionCode) {
        int hashcode = 0;
        String requestStr = "http://192.168.69.178:8200/HD2/jd/" + requestCode;

        Encrypt enc = new Encrypt("53174eac56fd7535901a7f517edc6280672f67099650516c53f8");
        String enData = enc.encrypt(data);
        hashcode = enData.hashCode();
        List<NameValuePair> lists = new ArrayList<NameValuePair>();
        NameValuePair dataPair = new BasicNameValuePair("data", enData);
        NameValuePair sPair = new BasicNameValuePair("s", "123123123123");
        NameValuePair tPair = new BasicNameValuePair("t", "123123123123");
        NameValuePair hPair = new BasicNameValuePair("h", hashcode + "");
        lists.add(dataPair);
        lists.add(sPair);
        lists.add(tPair);
        lists.add(hPair);

        result = httpPost(requestStr, lists);

        Logout.i("YCW", "data = " + data);
        Logout.i("YCW", "requestStr = " + requestStr);
        for (int i = 0; i < lists.size(); i++) {
            Logout.i("YCW", lists.get(i).getName() + " = " + lists.get(i).getValue());
        }
        Logout.i("YCW", "result = " + result);
        return result;
    }

    /**
     * HTTP网路请求
     */
    private String httpPost(String requestStr, List<NameValuePair> lists) {
        String result = "";
        try {
            HttpEntity requestHttpEnity = new UrlEncodedFormEntity(lists);
            HttpPost httpPost = new HttpPost(requestStr);
            httpPost.setEntity(requestHttpEnity);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpPost);
            if (null == response) {
                return null;
            }
            HttpEntity httpEntity = response.getEntity();
            InputStream is = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while (null != (line = reader.readLine())) {
                result += line;
            }
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            result = e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return result;

    }
}
