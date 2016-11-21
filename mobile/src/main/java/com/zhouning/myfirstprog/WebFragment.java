package com.zhouning.myfirstprog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhouning.myfirstprog.tool.Logout;
import com.zhouning.myfirstprog.view.MyWebView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link WebFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String url, jumpParam, jumpType;

    public WebFragment() {
        // Required empty public constructor
    }

    public WebFragment(String url, String jumpParam, String jumpType) {
        this.url = url;
        this.jumpParam = jumpParam;
        this.jumpType = jumpType;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WebFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WebFragment newInstance(String param1, String param2) {
        WebFragment fragment = new WebFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("SYS", "onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    MyWebView webView;

    @Override
    public void onStart() {
        super.onStart();
        Log.i("SYS", "onStart");

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("SYS", "onCreateView");
        webView = new MyWebView(getActivity(), url, jumpParam, jumpType);
        webView.initWebView();
        if (url != null && url.endsWith("null")) {
            webView.loadUrl("file:///android_asset/404/index.html");
            Logout.i("数据请求失败或者页面找不到");
        } else {
            webView.loadUrl(url);
        }
        return webView;
    }
}
