package com.zhouning.myfirstprog;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhouning.myfirstprog.tool.Logout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadJsonFile extends AppCompatActivity {

    TextView resultT;
    String resultS;
    String savePath;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_read_json_file);
        resultT = (TextView) findViewById(R.id.readresult);
        savePath = Environment.getExternalStorageDirectory() + "/sgcc/myjson/";
        if (Build.VERSION.SDK_INT >= 23) {
            int checkStoragePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkStoragePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                readFile(null);
            }
        } else {
            readFile(null);
        }

    }
    public void readFile(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                File file = new File(Environment.getExternalStorageDirectory() + "/json.txt");
                StringBuffer sb = new StringBuffer();
                if (true) {
//                    return;
                    String requestCode = "GDT09080";
                    String data = "{\"appZipFileName\":\"\",\"appVersion\":\"1\",\"deviceType\":\"1\"}";
                    String versionCode = "";
                    String returnData = Connect.getInstance().connect(data, requestCode, versionCode);
                    resultS = returnData;
                    try {
                        JSONObject jsonObject = new JSONObject(returnData);
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        String rReturnCode = jsonData.getString("returnCode");
                        String rRequestCode = jsonData.getString("requestCode");
                        String rReturnMsg = jsonData.getString("returnMsg");
                        if (rReturnCode != null && !"".equals(rReturnCode) && "0000".equals(rReturnCode)) {

                            JSONObject jsonConfig = jsonData.getJSONObject("config");
                            String cConfigUpdate = jsonConfig.getString("configupdate");
                            String cConfigDownlUrl = jsonConfig.getString("configDownlUrl");
                            Boolean success = DownLoad.download(cConfigDownlUrl, savePath);
                            Boolean unzip = DownLoad.unZipFile(savePath, "myjson", "zsdl1234");
                            if (success && unzip)
                                handler.sendEmptyMessage(1);
                            else
                                handler.sendEmptyMessage(2);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Logout.i("YCW", e.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logout.i("YCW", e.getMessage());
                    }
                    handler.sendEmptyMessage(0);
                    return;
                }

                try {
//                    BufferedReader br = new BufferedReader(new FileReader(file));
                    InputStreamReader is = new InputStreamReader(new FileInputStream(file), "GBK");
                    BufferedReader br = new BufferedReader(is);
                    String readline = "";
                    while ((readline = br.readLine()) != null) {
                        sb.append(readline);
                    }
                    br.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    resultS = e.getMessage();
                    handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                    resultS = e.getMessage();
                    handler.sendEmptyMessage(0);
                }

                resultS = sb.toString();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    resultT.setText(resultS);
//                    doParse();
                    break;
                case 1:
                    Toast.makeText(context, "download success", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(context, "download fail", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    public void doParse() {
        try {
            JSONObject jsonObject = new JSONObject(resultS);
            JSONArray jsonArray = jsonObject.getJSONArray("menu");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonObject.getJSONObject("menu");
                String menuStates = jsonData.getString("menuStates");
                String closePrompt = jsonData.getString("closePrompt");
                String isBind = jsonData.getString("isBind");
                JSONArray menChildList = jsonData.getJSONArray("menChildList");

            }
//            String returnCode = jsonData.getString("returnCode");
//            String returnMsg:
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {//获取权限
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFile(null);
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
