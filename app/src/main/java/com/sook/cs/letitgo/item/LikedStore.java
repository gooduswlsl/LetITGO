package com.sook.cs.letitgo.item;

public class LikedStore {
    private int seq;
    private int sSeq;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }


    public int getsSeq() {
        return sSeq;
    }

    public void setsSeq(int mSeq) {
        this.sSeq = mSeq;
    }

    @Override
    public String toString() {
        return "LikedStore{" +
                "seq=" + seq +
                ", sSeq=" + sSeq +
                '}';
    }
}