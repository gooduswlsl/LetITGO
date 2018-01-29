package com.sook.cs.letitgo.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Menu implements Serializable {
    String mName;
    String mImgUrl;
    int mPrice;
    String mDetail;
    String sName;
    String sSite;

    public String getmName() {
        return mName;
    }

    public String getmImgUrl() {
        return mImgUrl;
    }

    public int getmPrice() {
        return mPrice;
    }

    public String getmDetail() {
        return mDetail;
    }

    public String getsName() {
        return sName;
    }

    public String getsSite() {
        return sSite;
    }


    @Override
    public String toString() {
        return "Menu{" +
                "mName='" + mName + '\'' +
                ", mImgUrl='" + mImgUrl + '\'' +
                ", mPrice=" + mPrice +
                ", mDetail='" + mDetail + '\'' +
                ", sName='" + sName + '\'' +
                ", sSite='" + sSite + '\'' +
                '}';
    }


}