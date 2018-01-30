package com.sook.cs.letitgo.item;

public class Member {
    public int seq;
    public String phone;
    public String group;

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPhone() {
        return phone;
    }


    public String getGroup() {
        return group;
    }

    public int getSeq() {
        return seq;
    }

    @Override
    public String toString() {
        return "Member{" +
                "seq=" + seq +
                ", phone='" + phone + '\'' +
                ", group='" + group + '\'' +
                '}';
    }

}