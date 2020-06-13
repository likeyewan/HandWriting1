package com.example.handwriting.other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class GetData {
    //读取数据
    public  String readJsonFile(String p) {
        StringBuilder sb = new StringBuilder();
        try {
            File file;
            if(p=="") {
                 file = new File(Environment.getExternalStorageDirectory()
                        .getCanonicalPath().toString()
                        + "/HandWriting/PointData"
                        + "/config.txt");
            }else {
                file = new File(p);
            }
            InputStream in = null;
            in = new FileInputStream(file);
            int tempbyte;
            while ((tempbyte = in.read()) != -1) {
                sb.append((char) tempbyte);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String succeedStr="";
        try {
            succeedStr = new String(sb.toString().getBytes("iso8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return succeedStr;
    }
    public Bitmap getBitmap(String path) {
        File mFile = new File(path);
        //若该文件存在
        if(mFile!=null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            return bitmap;
        }else
        return null;
    }
}
