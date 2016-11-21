package com.zhouning.myfirstprog;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Download数据类
 * 下载http指定服务器数据
 *
 * @see DownLoad#download(String, String) ,
 */
public class DownLoad {

    /** 用于ftp服务器，Http服务器，此方法可以下载图片压缩文件等大小比较小的文件,缓存为4k **/
    /**
     * @param downloadUrl 下载地址
     * @param savePath    保存地址
     * @return 返回成功标志 true 成功，false 失败
     * @throws Exception
     */
    public static boolean download(String downloadUrl, String savePath) throws Exception {
        String fileName = "";
        URL url = new URL(downloadUrl);
        // 打开链接
        // 指定一个下载的目标链接，然后构建一个URL对象，调用该 对象的openConnection方法来建立一个数据通路（连接）。
        URLConnection connection = url.openConnection();
        // 设置长链接
        connection.setRequestProperty("Connection", "Kepp-Alive");
        // 设置连接超时
        connection.setConnectTimeout(60 * 1000);
        // 输入流
        // 代码的最后一行使用
        // connection.getInputStream，拿到一个输入流对象，通过这个流对象我们就可以读取到这个文件的内容了。下面要做的，就是读取这个流，将流写入我
        // 们的本地文件
        InputStream is = connection.getInputStream();
        // 获取文件的长度
        int contentLength = connection.getContentLength();
        // 保存文件目录，并先判断文件夹是是否存在，如果不存在，则新建一个文件夹
        String dir = savePath;
        File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
        } else {
            deleteOldFile(f);
            f.mkdirs();
        }
        // 保存的文件名
        fileName = downloadUrl
                .substring(downloadUrl.lastIndexOf("/") + 1);
        fileName = "myjson.zip";
        File file = new File(dir + fileName);
        if (!new File(dir + fileName.substring(0, fileName.lastIndexOf("."))).exists()) {
            new File(dir + fileName.substring(0, fileName.lastIndexOf("."))).mkdirs();
        }
        if (file.exists()) {//文件已存在，但是不可能，在上面，文件夹存在时就删除文件夹了
            // Toast.makeText(TestZip.this, "文件已存在", Toast.LENGTH_LONG).show();
        } else {
            // 下载
            // 4k的数据缓冲
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(dir + fileName);
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            os.close();
            is.close();
        }
        return true;
    }

    /**
     * 删除旧文件
     *
     * @param old
     */
    private static void deleteOldFile(File old) {
        if (old.exists()) {//判断原来文件（细化到菜单）是否存在
            if (old.isFile()) {//判断是否是文件
                old.delete();
            } else if (old.isDirectory()) {//否则如果它是一个目录
                File files[] = old.listFiles();//声明目录下所有的文件
                for (int i = 0; i < files.length; i++) {
                    deleteOldFile(files[i]);//把每个文件
                }
            }
            old.delete();
        }
    }

    public static boolean unZipFile(String path, String filename, String pass) {
        try {
            ZipFile zipFile = new ZipFile(path + "/" + filename + ".zip");
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(pass);
            }
            zipFile.extractAll(path + "/" + filename);
        } catch (ZipException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
