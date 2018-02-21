package com.sook.cs.letitgo.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Seller implements Serializable {
    public int seq;
    public String name;
    public String phone;
    public String img;
    public String site;
    public String tel;
    public String address;
    public String webpage;
    public double latitude;
    public double longitude;
    public String regId;

    public int getSeq() {
        return seq;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getImg() {
        return img;
    }

    public String getSite() {
        return site;
    }

    public String getTel() {
        return tel;
    }

    public String getAddress() {
        return address;
    }

    public String getWebpage() {
        return webpage;
    }

    public double getLatitude() { return latitude; }

    public double getLongitude() {
        return longitude;
    }

    public String getRegId() { return regId; }

    public void setRegId(String regId) { this.regId = regId; }

    @Override
    public String toString() {
        return "Seller{" +
                "seq=" + seq +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", img='" + img + '\'' +
                ", site='" + site + '\'' +
                ", tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                ", webpage='" + webpage + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}