package com.example.plant.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;

public class plantBean implements Serializable {
    private static final long serialVersionUID=1L;
    private String imgBase64; // 图书图片的Base64编码字符串
    private transient Bitmap img;  //图书图片
    private String pname;  //图书编号
    private String type;  //图书名
    private String content; //作者
    private String env; //出版社
    private String eng; //图书简介
    public Bitmap getimg() {
        if (img == null && imgBase64 != null) {
            byte[] decodedBytes = Base64.decode(imgBase64, Base64.DEFAULT);
            img = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
        return img;
    }

    public void setimgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public String getpname() {
        return pname;
    }

    public void setpname(String pname) {
        this.pname = pname;
    }

    public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }

    public String getcontent() {
        return content;
    }

    public void setcontent(String content) {
        this.content = content;
    }

    public String getenv() {
        return env;
    }

    public void setenv(String env) {
        this.env = env;
    }

    public String geteng() {
        return eng;
    }
    public void seteng(String eng) {
        this.eng = eng;
    }
}
