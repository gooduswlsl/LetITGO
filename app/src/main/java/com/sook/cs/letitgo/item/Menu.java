package com.sook.cs.letitgo.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Menu implements Serializable {
    public String mName;
    public int mPrice;
    public String mDetail;
    public int seller_seq;
    public int action;
    public int mSeq;


    public String mImgUrl;

    public void setmSeq(int mSeq) {
        this.mSeq = mSeq;
    }

    public int getmSeq() {
        return mSeq;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImgUrl() {
        return mImgUrl;
    }

    public void setmImgUrl(String mImgUrl) {
        this.mImgUrl = mImgUrl;
    }

    public int getmPrice() {
        return mPrice;
    }

    public void setmPrice(int mPrice) {
        this.mPrice = mPrice;
    }

    public String getmDetail() {
        return mDetail;
    }

    public void setmDetail(String mDetail) {
        this.mDetail = mDetail;
    }

    public int getSeller_seq() {
        return this.seller_seq;
    }


    @Override
    public String toString() {
        return "Menu{" +
                "mName='" + mName + '\'' +
                ", mPrice=" + mPrice +
                ", mDetail='" + mDetail + '\'' +
                ", seller_seq=" + seller_seq +
                ", mSeq=" + mSeq +
                ", mImgUrl='" + mImgUrl + '\'' +
                '}';
    }
}