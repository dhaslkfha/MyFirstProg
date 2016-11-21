package com.zhouning.myfirstprog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This Demo is used for html5 and native programme research.
 */
public class MainActivity extends AppCompatActivity {
    Context context;
    EditText editText;
    Button okButton;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        context = this;
        editText = (EditText) findViewById(R.id.main_edittextid);
        okButton = (Button) findViewById(R.id.main_buttonid);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                handleWebView(str);
            }
        });
        webView = (WebView) findViewById(R.id.main_webviewid);
        //load local html page.
        handleWebView("file:///android_asset/sjhd/index.html");
    }

    /**
     * handle the webview
     */
    @SuppressLint({"JavaScriptInterface","SetJavaScriptEnabled"})
    private void handleWebView(String str) {
        WebSettings settings = webView.getSettings();
        //set javascript enable, allow webview handle javascript methods.
        settings.setJavaScriptEnabled(true);
        //must begin with http://
        webView.loadUrl(str.startsWith("file")?str:"http://"+str);
        //overwrite the setWebViewClient method, to deal with it'll jump to browser.
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
        webView.addJavascriptInterface(new myJSObject(),"sgcc");
    }
    public class myJSObject {
        @JavascriptInterface
        public void fromJsFunction(String str){
            if (str!=null&&str.startsWith("close")){
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }else
                Toast.makeText(MainActivity.this,"from h5 page: "+str,Toast.LENGTH_LONG).show();
            Log.d("L","fromJsFunction");
        }
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
