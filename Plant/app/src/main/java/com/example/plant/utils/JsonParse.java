package com.example.plant.utils;

import com.example.plant.bean.plantBean;
import com.example.plant.bean.plantBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class JsonParse {
    private static JsonParse instance;
    private JsonParse(){
    }
    public static JsonParse getInstance(){
        if(instance==null){
            instance=new JsonParse();
        }
        return  instance;
    }
    //解析图书信息
    public List<plantBean> getPlantList(String json){
        Gson gson=new Gson(); //使用gson库解析Json数据
        //创建一个TypeToken的匿名子类对象，并调用对象的getType()方法
        Type listType=new TypeToken<List<plantBean>>(){
        }.getType();
        List<plantBean> bookList=gson.fromJson(json,listType);
        return bookList;
    }


}
