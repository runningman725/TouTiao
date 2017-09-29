package com.example.toutiaotest.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.example.toutiaotest.activity.WebTouTiaoActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Admin on 2017/9/11.
 */

public class SaveBitmap {
    private final static String CACHE="/img";

    public SaveBitmap(WebTouTiaoActivity webTouTiaoActivity){

    }

    //保存图片的方法，保存到sdcard
    public static void saveImage(Bitmap bitmap, String imageName) throws IOException {
        String filePath = isExistsFilePath();
        FileOutputStream fos=null;
        File file = new File(filePath, imageName);
        try {
            fos = new FileOutputStream(file);
            if(fos!=null){
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //获取缓存文件夹目录 如果不存在创建 否则则创建文件夹 
    public static String isExistsFilePath() {
        String filePath=getSDPath()+CACHE;
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }
        return filePath;
    }

    //获取sd卡的缓存路径，一般在卡中sdCard就是这个目录 
    public static String getSDPath() {
        File sdDir=null;
        boolean sdCardExist = Environment.getExternalStorageState().
                equals(Environment.MEDIA_MOUNTED);
        if(sdCardExist){
            sdDir=Environment.getExternalStorageDirectory();//获取根目录
        }else{
            Log.d("Error", "没有内存卡");
        }
        return sdDir.toString();
    }

//    public static Bitmap getImageFromSDCard(String imageName){
//        String filepath=getSDPath()+CACHE+"/"+imageName;
//        File file = new File(filepath);
//        if(file.exists()){
//            Bitmap bm = BitmapFactory.decodeFile(filepath);
//            return bm;
//        }
//        return null;
//    }
}
