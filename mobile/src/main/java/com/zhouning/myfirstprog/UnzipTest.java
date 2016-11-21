package com.zhouning.myfirstprog;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.util.ArrayList;

/**
 * this file is test the zip and unzip files function.and also file encryption.
 */
public class UnzipTest extends AppCompatActivity implements View.OnClickListener {

    Button zip,unzip,delete,downLoad;
    int i = 0;
    Context context;
    String path = "/data/data/com.zhouning.myfirstprog/sgcc";
    String downloadpath = "http://192.168.69.178:8200/filesys/h5rdl/sj.zip";
    String savepath = "";
    String filename = "sjhd";
    String pass = "111111";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unzip_test);
        context = this;
        savepath = Environment.getExternalStorageDirectory()+"/sgcc/";
        zip = (Button)findViewById(R.id.unzip_button1) ;
        zip.setOnClickListener(this);
        unzip = (Button)findViewById(R.id.unzip_button2) ;
        unzip.setOnClickListener(this);
        delete = (Button)findViewById(R.id.unzip_button3) ;
        delete.setOnClickListener(this);
        downLoad = (Button)findViewById(R.id.download_button4);
        downLoad.setOnClickListener(this);

    }
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            switch (i){
                case 1:
                    createZipFile(path,filename,pass);
                    break;
                case 2:
                    unZipFile(path,filename,pass);
                    break;
                case 3:
                    deleteUnZipFile(path,filename);
                    break;
                case 4:
                    boolean b = false;
                    try {
                        b = DownLoad.download(downloadpath,savepath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (b){
                        handler.sendEmptyMessage(6);
                    }else {
                        handler.sendEmptyMessage(7);
                    }
                    break;
            }
        }
    });
    Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(context,"create zip file finish",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(context,"create zip file error",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(context,"unzip file finish",Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                    break;
                case 3:
                    Toast.makeText(context,"unzip file error",Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(context,"delete file finish",Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    Toast.makeText(context,"delete file error",Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    Toast.makeText(context,"download file finish",Toast.LENGTH_LONG).show();
                    break;
                case 7:
                    Toast.makeText(context,"download file error",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    /**
     * create zip file.
     */
    private void createZipFile(String path,String filename,String pass){
        try {
            //create zip file and point out the path and name.
            File zipfiledir = new File(path);
            if (!zipfiledir.exists())
                zipfiledir.mkdirs();

            //delete the exists old zip file.
            File zipfile = new File(path+"/"+filename+".zip");
            if (zipfile.exists())
                zipfile.delete();

            ZipFile zipFile = new ZipFile(path+"/"+filename+".zip");
            ArrayList<File> fileAddZip = new ArrayList<File>();
            //add a file to zip file
            fileAddZip.add(new File(Environment.getExternalStorageDirectory()+"/18.png"));
            //zip's parameters
            ZipParameters parameters = new ZipParameters();
            //is encrypt or not
            parameters.setEncryptFiles(true);
            //zip style:deflate way
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            //zip level:normal
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            //encrypt level:standard
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            //zip file password
            parameters.setPassword(pass);
            //create zipFile
            zipFile.createZipFile(fileAddZip,parameters);

            handler.sendEmptyMessage(0);

        } catch (ZipException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(1);
        }catch(Exception e){
            e.printStackTrace();
            handler.sendEmptyMessage(1);
        }

    }
    public boolean unZipFile(String path,String filename,String pass){
        try {
            ZipFile zipFile = new ZipFile(savepath+"myjson.zip");
//            ZipFile zipFile = new ZipFile(path+"/"+filename+".zip");
            if (zipFile.isEncrypted()){
                zipFile.setPassword("zsdl1234");
            }
            zipFile.extractAll(savepath+"myjson");
            handler.sendEmptyMessage(2);
        } catch (ZipException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(3);
        }
        return true;
    }

    /**
     * delete unzipped file
     * @param path unzipped file path
     * @return
     */
    public boolean deleteUnZipFile(String path,String filename){
        try {
            path = path+"/"+filename;
            File file = new File(path);
            deleteDir(file);
            handler.sendEmptyMessage(4);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            handler.sendEmptyMessage(5);
        }
        return true;
    }

    /**
     * delete folders
     * @param file
     */
    private void deleteDir(File file){
        if (file.exists()){
            if (file.isFile())
                file.delete();
            else if (file.isDirectory()){
                File files[] = file.listFiles();
                for (int i = 0;i<files.length;i++){
                    deleteDir(files[i]);
                }
            }
            file.delete();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.unzip_button1:
                i = 1;
                thread.start();
                break;
            case R.id.unzip_button2:
                i = 2;
                thread.start();
                break;
            case R.id.unzip_button3:
                i = 3;
                thread.start();
                break;
            case R.id.download_button4:
                i = 4;
                thread.start();
                break;
                default:
                    break;
        }
    }
}
