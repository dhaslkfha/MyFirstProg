package com.zhouning.myfirstprog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TOM on 16/11/18.
 * 此类提供所有H5页面调用APP的数据的方法
 */

public class GetAppData {
    public static GetAppData getAppData;
    Context context;

    /**
     * use singleton prefer static function.
     * @param context
     * @return
     */
    public static GetAppData getInstance(Context context){
        if (getAppData==null){
            getAppData = new GetAppData(context);
        }
        return getAppData;
    }
    GetAppData(Context context){
        this.context = context;
    }
    /**
     * get the data h5 needed.
     * @param order
     * @return needed data
     */
    public String getNeededData(@NonNull String order, @Nullable String param) {//app provide data to h5
        String str = "";
        if ("getConsInfo".equals(order)){//get default cons info.
            str = getConsInfo();
        }else if ("getUserInfo".equals(order)){//get user login info
            str = getUserInfo();
        }else if ("getConfigInfo".equals(order)){//get config info
            str = getConfigInfo();
        }else if ("getAppInfo".equals(order)){//get app and system info
            str = getAppInfo();
        }else if ("getConsListInfo".equals(order)){//get all cons info
            str = getConsListInfo();
        }else if ("getAreaListInfo".equals(order)){//get all area info
            str = getAreaListInfo(param);
        }else if ("getOrgListInfo".equals(order)){//get all org info
            str = getOrgListInfo(param);
        }
        return str;
    }

    /**
     * getOrgListInfo
     * @param param
     * @return all org info base on param
     */
    private String getOrgListInfo(String param) {
        JSONObject mOrder = new JSONObject();
        JSONObject mData = new JSONObject();
        try {
            mOrder.put("requestCode", "getOrgListInfo");
            JSONObject subJsonObject = new JSONObject();
            subJsonObject.put("orgNo", "110100");
            subJsonObject.put("orgName","西城供电公司");
            JSONArray mArray = new JSONArray();
            mArray.put(subJsonObject);
            mData.put("orgList", mArray);
            mOrder.put("data", mData);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mOrder.toString();
    }

    /**
     * getAreaListInfo
     * @param param
     * @return all area info base on param
     */
    private String getAreaListInfo(String param) {
        JSONObject mOrder = new JSONObject();
        JSONObject mData = new JSONObject();
        try {
            mOrder.put("requestCode", "getAreaListInfo");
            JSONObject subJsonObject = new JSONObject();
            subJsonObject.put("areaNo", "1101200");
            subJsonObject.put("areaName","北京市");
            JSONArray mArray = new JSONArray();
            mArray.put(subJsonObject);
            mData.put("areaList", mArray);
            mOrder.put("data", mData);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mOrder.toString();
    }

    /**
     * getConsListInfo
     * @return all cons info.
     */
    private String getConsListInfo() {
        JSONObject mOrder = new JSONObject();
        JSONObject mData = new JSONObject();
        try {
            mOrder.put("requestCode", "getConsListInfo");
            JSONObject subJsonObject = new JSONObject();
            subJsonObject.put("consNo", "101010101");
            subJsonObject.put("consName","张三");
            subJsonObject.put("consAddr","北京丰台时代财富");
            subJsonObject.put("orgNo","110101");
            subJsonObject.put("isDefaultCons",1);
            subJsonObject.put("isOuthorizeCons",1);
            JSONArray mArray = new JSONArray();
            mArray.put(subJsonObject);
            mData.put("consList", mArray);
            mOrder.put("data", mData);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mOrder.toString();
    }

    /**
     * getAppInfo
     * @return app and system info.
     */
    private String getAppInfo() {
        JSONObject mOrder = new JSONObject();
        JSONObject mData = new JSONObject();
        try {
            mOrder.put("requestCode", "getAppInfo");
            mData.put("appVersion", "3.1.5");
            mData.put("systemVersion","6.0.1");
            mData.put("systemType","android");
            mOrder.put("data", mData);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mOrder.toString();
    }

    /**
     * getConfigInfo
     * @return config info like terminalCode.
     */
    private String getConfigInfo() {
        JSONObject mOrder = new JSONObject();
        JSONObject mData = new JSONObject();
        try {
            mOrder.put("requestCode", "getConfigInfo");
            mData.put("firmsCode", "渠道商编号");
            mData.put("spotCode","网点编号");
            mData.put("operatorCode","操作员编号");
            mData.put("terminalCode","终端编号");
            mOrder.put("data", mData);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mOrder.toString();
    }

    /**
     * getUserInfo
     * @return user info
     */
    private String getUserInfo(){
        JSONObject mOrder = new JSONObject();
        JSONObject mData = new JSONObject();
        try {
            mOrder.put("requestCode", "getUserInfo");
            mData.put("userName", "张三");
            mData.put("phoneNo","15520081111");
            mData.put("userId","43127");
            mData.put("provinceNo","110101");
            mOrder.put("data", mData);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mOrder.toString();
    }
    /**
     * getConsInfo
     * @return ConsInfo in Json
     */
    private String getConsInfo(){
        JSONObject mOrder = new JSONObject();
        JSONObject mData = new JSONObject();
        try {
            mOrder.put("requestCode", "getConsInfo");
            mData.put("consNo", "1020185630");
            mData.put("consName","张三");
            mData.put("addr","北京丰台时代财富");
            mData.put("orgNo","110101");
            mOrder.put("data", mData);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mOrder.toString();
    }
}
