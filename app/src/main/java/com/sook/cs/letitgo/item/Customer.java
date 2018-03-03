package com.sook.cs.letitgo.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Customer implements Serializable {
    public int seq;
    public String name;
    public String phone;
    public String sextype;
    public String birthday;
    public String img;
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
    public String getSextype() {
        return sextype;
    }
    public String getBirthday() { return birthday; }
    public String getImg() {
        return img;
    }
    public void setSeq(int seq){ this.seq = seq; }
    public void setName(String name) { this.name = name; }
    public void setSextype(String sextype) { this.sextype = sextype; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setImg(String img) { this.img = img; }
    public String getRegId() { return regId; }
    public void setRegId(String regId) { this.regId = regId; }

    @Override
    public String toString() {
        return "Customer{" +
                "seq=" + seq +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", sextype='" + sextype + '\'' +
                ", birthday='" + birthday + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}