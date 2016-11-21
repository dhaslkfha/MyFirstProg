package com.zhouning.myfirstprog.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.zhouning.myfirstprog.R;


/**
 * Created by TOM on 16/11/17.
 */

public class LoadingDialog extends ProgressDialog{
    private Activity context;

    public LoadingDialog(Context context) {
        super(context, R.style.Dialog_BackgroundTransparent);
        this.context = (Activity) context;
//		this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }


    private boolean onlyCloseDialog=false;//false 关闭dialog的同时关闭页面 ，true仅关闭dialog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingdialog);
        onlyCloseDialog=false;
    }

    /**
     * 设置关闭点击返回时不关闭页面，需在show之后调用
     * @param expect
     */
    public void setOnlyCloseDialog(boolean expect){
        onlyCloseDialog=expect;

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.dismiss();
            Log.e("msg", "   onlyCloseDialog="+onlyCloseDialog);
            if(!onlyCloseDialog){
                Log.e("msg", "关闭当前页面  ");
                context.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
