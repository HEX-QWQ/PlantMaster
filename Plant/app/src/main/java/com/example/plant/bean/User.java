package com.example.plant.bean;

public class User {
    private String account;
    private String pwd;
    private String Sname;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSname() {
        return Sname;
    }

    public void setSname(String sname) {
        Sname = sname;
    }

    public User() {
        // 默认构造函数
    }

    public User(String account, String pwd, String sname) {
        this.account = account;
        this.pwd = pwd;
        Sname = sname;
    }

    //getter 和 setter 方法省略
}




