package com.sook.cs.letitgo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Store implements Serializable {
    String sName;
    String sBranch;
    String sPhone;
    String sWebpage;
    String sLocation;
    String sImgUrl;


    public int seq;
    public String name;
    public String phone;


    public String getsName() {
        return sName;
    }

    public String getsBranch() {
        return sBranch;
    }

    public String getsPhone() {
        return sPhone;
    }

    public String getsWebpage() {
        return sWebpage;
    }

    public String getsLocation() {
        return sLocation;
    }

    public String getsImgUrl() {
        return sImgUrl;
    }

    @Override
    public String toString() {
        return "Store{" +
                "seq=" + seq +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}