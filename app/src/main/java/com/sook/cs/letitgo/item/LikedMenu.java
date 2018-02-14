package com.sook.cs.letitgo.item;

public class LikedMenu {
    private int seq;
    private int mSeq;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }


    public int getmSeq() {
        return mSeq;
    }

    public void setmSeq(int mSeq) {
        this.mSeq = mSeq;
    }

    @Override
    public String toString() {
        return "LikedMenu{" +
                "seq=" + seq +
                ", mSeq=" + mSeq +
                '}';
    }
}