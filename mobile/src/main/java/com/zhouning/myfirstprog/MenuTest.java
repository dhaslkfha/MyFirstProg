package com.zhouning.myfirstprog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;

public class MenuTest extends Activity {

    Context context;
    GridView gridView;
    ArrayList<String> menuNames;
    ArrayList<Drawable> menuImages;
    MenuTestAdapter adapter;
    String path = "/data/data/com.zhouning.myfirstprog/sgcc";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_test);
        gridView = (GridView)findViewById(R.id.gridView2);
        context = this;
        menuImages = new ArrayList<Drawable>();
        menuImages.add(getResources().getDrawable(R.drawable.image));
        menuImages.add(getResources().getDrawable(R.drawable.firstladder));
        menuImages.add(getResources().getDrawable(R.drawable.logo_wechat));
        menuImages.add(getResources().getDrawable(R.drawable.electricityvalue));
        menuImages.add(getResources().getDrawable(R.drawable.paycardearphone));
        menuImages.add(getResources().getDrawable(R.drawable.guide5_bg));
        menuNames = new ArrayList<String>();
        menuNames.add("menu1");
        menuNames.add("menu2");
        menuNames.add("menu3");
        menuNames.add("menu4");
        menuNames.add("menu5");
        menuNames.add("menu6");
        adapter = new MenuTestAdapter(context,menuImages,menuNames);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==1||position==0){
                    startActivityForResult(new Intent(context,UnzipTest.class),1001);
                }else {
                    startActivity(new Intent(context,FragmentAddWebView.class));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            File file = new File(path+"/sjhd/18.png");
            if (file.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(path+"/sjhd/18.png");
                Drawable drawable = new BitmapDrawable(bitmap);
                menuImages.set(0, drawable);
            }
            File file1 = new File(path+"/sjhd/18.png");
            if (file1.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(path+"/sjhd/18.png");
                Drawable drawable = new BitmapDrawable(bitmap);
                menuImages.set(1, drawable);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
