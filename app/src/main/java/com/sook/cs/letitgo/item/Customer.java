package com.sook.cs.letitgo.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Store implements Serializable {
    public int seq;
    public String name;
    public String phone;
    public String img;
    public String site;
    public String tel;
    public String address;
    public String webpage;


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

    @Override
    public String toString() {
        return "Store{" +
                "seq=" + seq +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", img='" + img + '\'' +
                ", site='" + site + '\'' +
                ", tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                ", webpage='" + webpage + '\'' +
                '}';
    }
}