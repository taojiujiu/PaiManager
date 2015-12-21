package com.pgyer.paimanager.paimanager;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import java.io.File;
import java.util.List;

;

/**
 * Created by Tao9jiu on 15/11/28.
 */
public class DataReader {
//    static List<ItemVideo> lists=new ArrayList<ItemVideo>();
//    static boolean ischange;

    public static List<ItemVideo> readerdata(List<ItemVideo> lists){
        String path;
        path = PathUtilReader.getPaiMasterVideoPath().toString();

        File[] files = new File(path).listFiles();

             if(lists.size() != files.length || lists.isEmpty()){


                  for(int i = 0; i<files.length; i++) {
                  File file;
                  file = files[i];
                  if(file.isFile()) {

                    String name = file.getName();
                    Bitmap bitmap, image;
                    bitmap = ThumbnailUtils.createVideoThumbnail(file.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
                    image = ThumbnailUtils.extractThumbnail(bitmap, 60, 60, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                    ItemVideo items = new ItemVideo();
                    items.setName(name);
                    items.setImage(image);
                    items.setPath(file.getAbsolutePath());
                    double size = FileSizeUtil.getFileOrFilesSize(file.getPath(),FileSizeUtil.SIZETYPE_KB);
                    items.setSize((int) size);
                    lists.add(items);
                   }
                }
            }


        return lists;

    }

    public static void DeleteViedoFile(String path){
        File file = new File(path);
        file.delete();
    }

//    public static boolean getChanged(){
//        String path;
//        path = PathUtilReader.getPaiMasterVideoPath().toString();
//
//        File[] files = new File(path).listFiles();
//        return ischange = lists.size() != files.length ? true : false;
//    }

//    public static String searchFiles(){
//        String path;
//        path = ReaderPathUtil.getPaiMasterVideoPath().toString();
//        String result = null;
//        File[] files = new File(path).listFiles();
//        for (File file : files) {
//                result += file.getPath() + "\n";
//
//        }
//        if (result.equals("")){
//            result = "找不到文件!!";
//        }
//        return result;
//    }





}
