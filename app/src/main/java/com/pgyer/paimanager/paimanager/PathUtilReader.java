package com.pgyer.paimanager.paimanager;

import android.os.Environment;

import java.io.File;

/**
 * Created by Tao9jiu on 15/11/27.
 */
public class PathUtilReader {

    public static boolean SdkIsExist;
    public static String SdkRootPath;
    public static String PaiMasterVideoPath;

    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

/**
 * 获取手机自身sdk根目录
 *
 */
    public static String getSdkPath(){
        SdkRootPath=Environment.getExternalStorageDirectory().getPath();
//        Log.d("tao99",SdkRootPath);

        return SdkRootPath.toString();
    }

/***
* 获得拍大师video存储目录
*/
    public static String getPaiMasterVideoPath(){
        getSdkPath();
        String path = "/paimaster/video";
        PaiMasterVideoPath = SdkRootPath+path;
        File file = new File(PaiMasterVideoPath);
        if(file.exists()){
;            return  PaiMasterVideoPath;
        }else{
            return PaiMasterVideoPath="tao";
        }
    }



/***
 * 内置和外置存储卡路径获取
 */
//    public static  final  void  getSdkPath(){
//        SdkIsExist = isSdCardExist();
//
//        StringBuilder log = new StringBuilder();
//        String inPath = getInnerSDCardPath();
//        log.append("内置SD卡路径：" + inPath + "\r\n");
//
//        List<String> extPaths = getExtSDCardPath();
//        for (String path : extPaths) {
//            log.append("外置SD卡路径：" + path + "\r\n");
//        }
//        System.out.println(log.toString());
//
//    }
//
//    public static String getInnerSDCardPath() {
//        return Environment.getExternalStorageDirectory().getPath();
//    }
//
//    /**
//     * 获取外置SD卡路径
//     * @return  应该就一条记录或空
//     */
//    public static List<String> getExtSDCardPath()
//    {
//        List<String> lResult = new ArrayList<String>();
//        try {
//            Runtime rt = Runtime.getRuntime();
//            Process proc = rt.exec("mount");
//            InputStream is = proc.getInputStream();
//            InputStreamReader isr = new InputStreamReader(is);
//            BufferedReader br = new BufferedReader(isr);
//            String line;
//            while ((line = br.readLine()) != null) {
//                if (line.contains("extSdCard"))
//                {
//                    String [] arr = line.split(" ");
//                    String path = arr[1];
//                    File file = new File(path);
//                    if (file.isDirectory())
//                    {
//                        lResult.add(path);
//                    }
//                }
//            }
//            isr.close();
//        } catch (Exception e) {
//        }
//        return lResult;
//    }

}
