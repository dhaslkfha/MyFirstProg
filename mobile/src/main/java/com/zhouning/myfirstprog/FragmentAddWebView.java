package com.zhouning.myfirstprog;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Stack;

public class FragmentAddWebView extends AppCompatActivity {

    private FrameLayout layout;
    WebFragment fragment;
    private FragmentManager manager;
    Stack<WebFragment> stack;
    String downloadpath = "http://192.168.69.178:8200/filesys/h5rdl/sj.zip";
    String savepath = "";
    String filename = "";
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmentaddwebview);
        context = this;
        savepath = Environment.getExternalStorageDirectory()+"/sgcc/";
        filename = "sj";
//        download();
        layout = (FrameLayout) findViewById(R.id.myfragement);
        manager = getSupportFragmentManager();
        stack = new Stack<WebFragment>();
        addFragment("");
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
//        return super.onKeyLongPress(keyCode, event);
        return true;
    }


    Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case 0:
                    addFragment("file:///"+savepath+filename+"/index.html");
                    Toast.makeText(context,"success",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(context,"Unzip fail",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(context,"Download fail",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private void download() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (DownLoad.download(downloadpath,savepath)){
                        if (DownLoad.unZipFile(savepath,filename,"")){
                            handler.sendEmptyMessage(0);
                        }else {
                            handler.sendEmptyMessage(1);
                        }
                    }else {
                        handler.sendEmptyMessage(2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("YCW",e.getMessage());
                }
            }
        }).start();
    }

    /**
     *
     */
    private void addFragment(String path){
        fragment = new WebFragment("file:///android_asset/localsj/index.html","","");
//        fragment = new WebFragment("file:///android_asset/yongdianshenqing/index.html","","");
//        fragment = new WebFragment(path,"","");
        manager.beginTransaction().add(R.id.myfragement,fragment).commit();
    }


    public void jumpNext(String url,String jumpParam,String jumpType){
        manager.beginTransaction().hide(fragment);
        fragment = new WebFragment(url,jumpParam,jumpType);
        manager.beginTransaction().add(R.id.myfragement,fragment).commit();
        stack.push(fragment);
    }
    public void backPre(){

        if (stack.size()>0){
            manager.beginTransaction().remove(stack.pop()).commit();
        }else {
            finish();
        }

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (stack.size()>0){
                manager.beginTransaction().remove(stack.pop()).commit();
                return true;
            }else {
                finish();
                return  true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
