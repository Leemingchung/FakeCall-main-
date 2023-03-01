package com.example.fakecall.Fagment;

import android.widget.ImageView;

public class TabViewDataSingleton {
    private static String name;
    private static String sdt;
    private static  byte[] img  ;

    private  static ImageView imgsave ;

    public static ImageView getImgsave() {
        return imgsave;
    }

    public static void setImgsave(ImageView imgsave) {
        TabViewDataSingleton.imgsave = imgsave;
    }

    public static byte[] getImg() {
        return img;
    }

    public static void setImg(byte[] img) {
        TabViewDataSingleton.img = img;
    }

    public static String getSdt() {
        return sdt;
    }
    public static void setSdt(String sdt) {
        TabViewDataSingleton.sdt = sdt;
    }
    public static String getName() {
        return name;
    }
    public static void setName(String name) {
        TabViewDataSingleton.name = name;
    }
    //
    private static TabViewDataSingleton instance = null;
    protected TabViewDataSingleton() {
        // Exists only to defeat instantiation.
    }
    public static TabViewDataSingleton getInstance() {
        if(instance == null) {
            instance = new TabViewDataSingleton();
        }
        return instance;
    }
}
