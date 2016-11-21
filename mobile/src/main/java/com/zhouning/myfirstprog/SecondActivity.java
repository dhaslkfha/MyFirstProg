package com.zhouning.myfirstprog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    Context context;
    Button button;
    WebView webView;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        context = this;
        handler = new Handler();
        /**
         * @see button
         */
        button = (Button) findViewById(R.id.second_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "from second activity";
                //call methods in js page.
//                webView.loadUrl("javascript:showJson('"+str+"')");
//                startActivity(new Intent(SecondActivity.this, H5.class));
                startActivity(new Intent(SecondActivity.this, MenuTest.class));
            }
        });
        webView = (WebView) findViewById(R.id.second_webView);
        handleWebView();
    }

    private void handleWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new HandleJSCall(), "sgcc");
        webView.setWebChromeClient(new WebChromeClient() {
            //handle the alert on h5 page. or it wouldn't work.
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                Toast.makeText(SecondActivity.this, "Confirm.", Toast.LENGTH_LONG).show();
                return super.onJsConfirm(view, url, message, result);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String str = "from second activity";
                //call methods in js page.
                webView.loadUrl("javascript:showJson('"+str+"')");
            }
        });
//        webView.loadUrl("file:///android_asset/jscall/index.html");
        webView.loadUrl("file:///"+ Environment.getExternalStorageDirectory()+"/sgcc/sj/index.html");
//        webView.loadUrl("file:///android_asset/cs/index.html");
    }


    /**
     * {@link MainActivity#handleWebView(String)}
     * {@link com.zhouning.myfirstprog.MainActivity.myJSObject}
     */
    class HandleJSCall {
        @JavascriptInterface
        public void fromJsFunction(@Nullable String str) {
            Toast.makeText(SecondActivity.this,"jump to https page test",Toast.LENGTH_LONG).show();
//            startActivity(new Intent(SecondActivity.this,SSLPageTest.class));
            String str2 = "from second activity";
            //call methods in js page.
           handler.post(new Runnable() {
               @Override
               public void run() {
                   webView.loadUrl("javascript:showJson('123')");
               }
           });

        }

    }
}
