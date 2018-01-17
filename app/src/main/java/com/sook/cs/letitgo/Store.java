package com.sook.cs.letitgo;

import java.io.Serializable;
@SuppressWarnings("serial")
public class Store implements Serializable{
    String sName;
    String sBranch;
    String sPhone;
    String sWebpage;
    String sLocation;
    String sImgUrl;

    public Store(String sName, String sBranch, String sPhone, String sWebpage, String sLocation, String sImgUrl) {
        this.sName = sName;
        this.sBranch = sBranch;
        this.sPhone = sPhone;
        this.sWebpage = sWebpage;
        this.sLocation = sLocation;
        this.sImgUrl = sImgUrl;
    }

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

}