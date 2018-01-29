package com.sook.cs.letitgo.item;

public class Member {
    public String phone;
    public String group;

    public String getPhone() {
        return phone;
    }

    public String getGroup(){
        return group;
    }

    @Override
    public String toString() {
        return "Member{" +
                "phone='" + phone + '\'' +
                ", group=" + group + '\'' +
                '}';
    }
}