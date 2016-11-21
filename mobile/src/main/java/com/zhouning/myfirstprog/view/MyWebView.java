package com.zhouning.myfirstprog.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.zhouning.myfirstprog.Connect;
import com.zhouning.myfirstprog.FragmentAddWebView;
import com.zhouning.myfirstprog.GetAppData;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by 宁 on 2016/9/20.
 * Define the WebView handle the h5 page.
 * Include show,jump,transfer etc.
 * Create a constructor trans the params to MyWebView.
 * if want create a new webview, you should call the constructor function
 *
 * @see MyWebView#MyWebView(Context, String, String, String) ,
 * then init your webview
 * @see MyWebView#initWebView()
 */
public class MyWebView extends WebView {

    /**
     * context of the webview page.
     */
    Context context;
    /**
     * the webview that show the page.
     */
    WebView webView;
    /**
     * activity of the webview.
     */
    Activity activity;
    /**
     * handle js function.
     */
    Handler handler;
    /**
     * the root url of the website. url include index.html.
     */
    String rootUrl = "";
    /**
     * the params that jump to next page.
     */
    String jumpParam = "";
    /**
     * jump type
     * 1:jump to native page,
     * 2:jump to webview page without request server data,
     * 3:jump to webview page need request server data.
     */
    String jumpType = "2";
    /**
     * the activity show the page.
     */
    FragmentAddWebView h5;
    /**
     * the data add to page.
     */
    String addData = "";
    /**
     * loading dialog.
     */
    LoadingDialog dialog;

    /**
     * constructor to trans params to MyWebView.
     *
     * @param context
     * @param rootUrl   root url of the series of pages.
     * @param jumpParam if there is a jump function called by fromJsFunction_jump
     *                  then jumpParam must back to new MyWebView Page by this.
     * @param jumpType  jump type of the function fromJsFunction_jump()
     * @see com.zhouning.myfirstprog.WebFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     * @see MyWebView.HandleJSCall#fromJsFunction_jump(String)
     */
    public MyWebView(Context context, String rootUrl, String jumpParam, String jumpType) {
        super(context);
        this.context = context;
        this.rootUrl = rootUrl;
        this.jumpParam = jumpParam;
        this.jumpType = jumpType;
        h5 = (FragmentAddWebView) context;
        handler = new Handler();
        activity = (Activity) context;
    }

    /**
     * init webview settings.
     */
    public void initWebView() {
        webView = this;
        dialog = new LoadingDialog(context);
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
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                activity.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                doJumpLoadData(jumpParam);
                jumpParam = "";
            }
        });
        /**
         * disable the long press of copy and paste.
         */
        webView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setLongClickable(false);
//        webView.setBackgroundColor(0);
    }

    /**
     * load jump data after webview page is finished.
     *
     * @param param
     */
    private void doJumpLoadData(final String param) {
        if (!"".equals(param)) {
            if ("2".equals(jumpType)) {//don't need request data
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "param = " + param, Toast.LENGTH_LONG).show();
                        webView.loadUrl("javascript:showJson('" + param + "')");
                    }
                });
            } else if ("3".equals(jumpType)) {//need request server data
                String[] strArray = handleTransferJson(param);
                final String requestCode = strArray[0];
                String versionCode = strArray[1];
                final String data = strArray[2];
                requestData(requestCode, versionCode, data, "");
            }
        }
    }

    /**
     * Object can receive call from h5 page.
     * H5页面调用WebView的所有方法
     */
    final class HandleJSCall {

        HandleJSCall() {
        }

        /**
         * ----close----
         * call when close the webview
         */
        @JavascriptInterface
        public void fromJsFunction_close() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ((Activity) context).finish();
                }
            });
        }

        /**
         * ----back----
         * call when press the back icon
         */
        @JavascriptInterface
        public void fromJsFunction_back() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    h5.backPre();
                }
            });
        }

        /**
         * ----init----
         * call when page want get some init info
         *
         * @param jsonStr
         */
        @JavascriptInterface
        public void fromJsFunction_init(@NonNull String jsonStr) {
//            Toast.makeText(context, "call fromJsFunction_init function, received data : "+jsonStr , Toast.LENGTH_LONG).show();
            String[] strArray = handleInitJson(jsonStr);
            String order = strArray[0];
            String param = strArray[1];
            final String data = GetAppData.getInstance(context).getNeededData(order, param);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:showJsoninit('" + data + "')");
                }
            });
        }

        /**
         * ----share----
         * call the share function
         *
         * @param jsonStr
         */
        @JavascriptInterface
        public void fromJsFunction_share(@NonNull String jsonStr) {
//            Toast.makeText(context, "call formJsFunction_share function", Toast.LENGTH_LONG).show();
            String[] strArray = handleShareJson(jsonStr);
            String title = strArray[0];
            String content = strArray[1];

        }

        /**
         * ----jump----
         * call when webview page jump to another webview page
         *
         * @param jsonStr
         */
        @JavascriptInterface
        public void fromJsFunction_jump(@NonNull String jsonStr) {
//            Toast.makeText(context, "call fromJsFunction_jump function", Toast.LENGTH_LONG).show();
            String[] strArray = handleJumpJson(jsonStr);
            jumpType = strArray[0];
            String url = strArray[1];
            jumpParam = strArray[2];
            final String suburl = rootUrl.substring(0, rootUrl.lastIndexOf("/") + 1) + url;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    h5.jumpNext(suburl, jumpParam, jumpType);
                }
            });
        }

        /**
         * ----transfer----
         * call when webview page transfer data with webservice
         *
         * @param jsonStr
         */
        @JavascriptInterface
        public void fromJsFunction_transfer(@NonNull String jsonStr, @Nullable String callBackFunc) {
            String[] strArray = handleTransferJson(jsonStr);
            final String requestCode = strArray[0];
            String versionCode = strArray[1];
            String data = strArray[2];
            requestData(requestCode, versionCode, data, callBackFunc);
        }
    }


    /**
     * handle share json
     *
     * @param json
     * @return share title and content
     */
    private String[] handleShareJson(String json) {
        String[] str = new String[2];
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject jsonObject = (JSONObject) tokener.nextValue();
            str[0] = jsonObject.getString("title");
            str[1] = jsonObject.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * handle init json
     *
     * @param json
     * @return init function order
     */
    private String[] handleInitJson(String json) {
        String[] str = new String[2];
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject jsonObject = (JSONObject) tokener.nextValue();
            str[0] = jsonObject.getString("order");
            if (tokener.more()) {
                JSONObject subObject = (JSONObject) tokener.nextValue();
                str[1] = subObject.getString("param");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * handle transfer json
     *
     * @param json
     * @return arrays of transfer data
     */
    private String[] handleTransferJson(String json) {
        String[] str = new String[3];
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject jsonObject = (JSONObject) tokener.nextValue();
            str[0] = jsonObject.getString("requestCode");
            str[1] = jsonObject.getString("versionCode");
            str[2] = jsonObject.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * handle jump json
     *
     * @param json
     * @return jump data
     */
    private String[] handleJumpJson(String json) {
        String[] str = new String[3];
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject jsonObject = (JSONObject) tokener.nextValue();
            str[0] = jsonObject.getString("type");
            str[1] = jsonObject.getString("url");
            str[2] = jsonObject.getString("param");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }

    Handler handler1 = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0://add server response data to webview.
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Bundle bundle = msg.getData();
                    final String function = bundle.getString("function");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if ("".equals(function)) {
                                webView.loadUrl("javascript:showJson('" + addData + "')");
                            } else {
                                webView.loadUrl("javascript:" + function + "('" + addData + "')");
                            }
                        }
                    });
                    break;
                case 1://show dialog while request server data.
                    if (dialog != null && !dialog.isShowing()) {
                        dialog.show();
                    }
                    break;
            }
        }
    };

    /**
     * 调用服务器，获取接口数据
     *
     * @param requestCode 请求交易代码
     * @param versionCode 版本号
     * @param data 入參
     * @param function 获取数据后，调用H5页面的方法名
     */
    private void requestData(final String requestCode, final String versionCode, final String data, final String function) {
        handler1.sendEmptyMessage(1);
        new Thread(new Runnable() {
            @Override
            public void run() {

                addData = Connect.getInstance().connect(data, requestCode, versionCode);
                Message msg = new Message();
                msg.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("function", function);
                msg.setData(bundle);
                handler1.sendMessage(msg);
            }
        }).start();
    }
}
