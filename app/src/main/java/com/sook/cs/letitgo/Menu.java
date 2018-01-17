package com.sook.cs.letitgo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Menu implements Serializable {
    String mName;
    String mImgUrl;
    int mPrice;
    String mDetail;
    String sName;
    String sBranch;

    public Menu(String mName, String mImgUrl, int mPrice, String mDetail, String sName, String sBranch) {
        this.mName = mName;
        this.mImgUrl = mImgUrl;
        this.mPrice = mPrice;
        this.mDetail = mDetail;
        this.sName = sName;
        this.sBranch = sBranch;
    }

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

    public String getsBranch() {
        return sBranch;
    }


}