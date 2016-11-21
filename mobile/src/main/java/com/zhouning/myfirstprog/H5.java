package com.zhouning.myfirstprog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.zhouning.myfirstprog.view.MyWebView;

public class H5 extends AppCompatActivity {
    MyWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new MyWebView(this,"file:///android_asset/sgccjs/index.html","","");
        setContentView(webView);

//        webView = (MyWebView) findViewById(R.id.black_webviewid);
        webView.initWebView();
//        webView.loadUrl("file:///android_asset/sj/lsyd.html");
//        Thread thread = new Thread();
//        try {
//            thread.sleep(2000);
//            thread.start();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        webView.loadUrl("file:///android_asset/sj/index.html");
        webView.loadUrl("file:///android_asset/sgccjs/index.html");
//        webView.loadUrl("file:///android_asset/cs/index.html");
//        webView.loadUrl("file:///android_asset/jscall/index.html");
//        webView.loadUrl("http://www.baidu.com");

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //to handle back key, to go back last page.
        if (KeyEvent.KEYCODE_BACK==keyCode&&webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
