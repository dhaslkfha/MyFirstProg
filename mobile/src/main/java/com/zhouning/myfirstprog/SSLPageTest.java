package com.zhouning.myfirstprog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLPageTest extends AppCompatActivity {

    WebView webView;
    TextView textView;
    Context context;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sslpage_test);
        context = this;
        textView = (TextView)findViewById(R.id.textView);
        webView = (WebView)findViewById(R.id.webView);
        handleWebView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                conncet();
            }
        }).start();

        /**
         * load https web page.
         */
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                webView.loadUrl("https://www.baidu.com");
            }
        });
    }

    private void handleWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            //handle the alert on h5 page. or it wouldn't work.
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case 0:
                    textView.setText("Https Page Content is: " + result);
                    break;
            }
        }
    };


    /**
     * 下面为HTTPS的请求方式
     */
    public void conncet(){
        String data = "{\"userGuid\":\"7601\"}";
        int hashcode = 0;
//        String dataStr ="s=123123123123&t=123123123123&data=\"+data+\"&h=\"+hashcode";
        String requestStr = "http://192.168.69.178:8200/HD2/jd/GDT09012";
        Map<String,String> map = new HashMap<String, String>();
        map.put("data",data);
        map.put("s","123123123123");
        map.put("t","123123123123");
        map.put("h",hashcode+"");

//        Encrypt enc = new Encrypt("E0FEE0FEF1FEF1FE");
        Encrypt enc = new Encrypt("53174eac56fd7535901a7f517edc6280672f67099650516c53f8");
        String enData = enc.encrypt(data);
        hashcode = enData.hashCode();
        List<NameValuePair> lists = new ArrayList<NameValuePair>();
        NameValuePair dataPair = new BasicNameValuePair("data",enData);
        NameValuePair sPair = new BasicNameValuePair("s","123123123123");
        NameValuePair tPair = new BasicNameValuePair("t","123123123123");
        NameValuePair hPair = new BasicNameValuePair("h",hashcode+"");
        lists.add(dataPair);
        lists.add(sPair);
        lists.add(tPair);
        lists.add(hPair);


//        result = post("https://www.baidu.com","","").toString();
//        result = post(requestStr,map2Url(map),"").toString();
//                result = requestStr;
        result = httpPost(requestStr,lists);
        Log.i("YCW","result = "+result);
        handler.sendEmptyMessage(0);

    }
    //HTTP
    private String httpPost(String requestStr, List<NameValuePair> lists){
        String result = "";
        try {
            HttpEntity requestHttpEnity = new UrlEncodedFormEntity(lists);
            HttpPost httpPost = new HttpPost(requestStr);
            httpPost.setEntity(requestHttpEnity);
            HttpClient httpClient = new DefaultHttpClient();
            Log.i("YCW",httpPost.getRequestLine().toString());
            Log.i("YCW",httpPost.getParams().toString());
            HttpResponse response = httpClient.execute(httpPost);
            if (null == response){
                return  null;
            }
            HttpEntity httpEntity = response.getEntity();
            InputStream is = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while(null!=(line=reader.readLine())){
                result +=line;
            }
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            result = e.getMessage();
        }catch (IOException e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return result;

    }

    //HTTPS
    private String post(String requestStr,String map,String s){
        StringBuffer bufferRes =null;
        try{
            HttpURLConnection http = initHttps(requestStr,"POST",null);
            OutputStream out = http.getOutputStream();
            out.write(map.getBytes(HTTP.UTF_8));
            out.flush();
            out.close();
            InputStream in = http.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in,HTTP.UTF_8));
            String valueString = null;
            bufferRes = new StringBuffer();
            while ((valueString=read.readLine())!=null){
                bufferRes.append(valueString);
            }
            in.close();
            if (http!=null){
                http.disconnect();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return bufferRes.toString();
    }

    private String  map2Url(Map<String,String> paramToMap){
        if (null==paramToMap||paramToMap.isEmpty()){
            return null;
        }
        StringBuffer url = new StringBuffer();
        boolean isfirst = true;
        for (Map.Entry<String,String> entry:paramToMap.entrySet()){
            if (isfirst){
                isfirst = false;
            }else {
                url.append("&");
            }
            url.append(entry.getKey()).append("=");
            String value = entry.getValue();
            if (!TextUtils.isEmpty(value)){
                try {
                    url.append(URLEncoder.encode(value, HTTP.UTF_8));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return url.toString();

    }

    private HttpURLConnection initHttps(String requestStr, String post, Map<String, String> map) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        TrustManager[] tm = {new CustomX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null,tm,new SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL _url = new URL(requestStr);
            HttpsURLConnection http = (HttpsURLConnection) _url.openConnection();
            http.setConnectTimeout(25000);
            http.setReadTimeout(25000);
            http.setRequestMethod(post);
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setRequestProperty("User-Agent","Mozilla/5.0(Window NT 6.3;WOW64)AppleWebKit/537.36(KHTML,like Gecko)Chrome/33.0.1750.146 Safari/537.36");
            if (null!=map&&!map.isEmpty()){
                for (Map.Entry<String,String> entry:map.entrySet()){
                    http.setRequestProperty(entry.getKey(),entry.getValue());
                }
            }
            http.setSSLSocketFactory(ssf);
            http.setDoOutput(true);
            http.setDoInput(true);
            http.connect();
            return http;
    }


    /**
     * 证书管理
     */
    class CustomX509TrustManager implements X509TrustManager{

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
//            return new X509Certificate[0];
            return null;
        }

    }

}
