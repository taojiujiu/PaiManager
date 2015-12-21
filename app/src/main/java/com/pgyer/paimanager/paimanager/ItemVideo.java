package com.pgyer.paimanager.paimanager;

import android.graphics.Bitmap;

/**
 * Created by Tao9jiu on 15/11/27.
 */
public class ItemVideo {


    String name;
    Bitmap image;
    String path;
    boolean ischeck = false;
//    boolean isUpload  ;
    int size;

    public ItemVideo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isIscheck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }


//    public boolean isUpload() {
//        return isUpload;
//    }
//
//    public void setUpload(boolean isUpload) {
//        this.isUpload = isUpload;
//    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
