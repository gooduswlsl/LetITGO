package com.sook.cs.letitgo.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Order implements Serializable {
    public int seq;
    public int cust_seq;
    public int seller_seq;
    public int menu_seq;
    public int num;
    public int price;
    public String time_order;
    public String time_take;
    public int permit;
    public String message;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getCust_seq() {
        return cust_seq;
    }

    public void setCust_seq(int cust_seq) {
        this.cust_seq = cust_seq;
    }

    public int getMenu_seq() {
        return menu_seq;
    }

    public void setMenu_seq(int menu_seq) {
        this.menu_seq = menu_seq;
    }

    public int getSeller_seq() {
        return seller_seq;
    }

    public void setSeller_seq(int seller_seq) {
        this.seller_seq = seller_seq;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTime_order() {
        return time_order;
    }

    public void setTime_order(String time_order) {
        this.time_order = time_order;
    }

    public String getTime_take() {
        return time_take;
    }

    public void setTime_take(String time_take) {
        this.time_take = time_take;
    }

    public int getPermit() {
        return permit;
    }

    public void setPermit(int permit) {
        this.permit = permit;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public String getTimeOrder() {
        String timeOrder = time_order.substring(0, 16);
        timeOrder = timeOrder.replace("-", ".");
        timeOrder = timeOrder.replace("T", "  ");
        return timeOrder;
    }

    public String getTimeTake() {
        String timeTake = time_take.substring(0, 16);
        timeTake = timeTake.replace("-", ".");
        timeTake = timeTake.replace("T", "  ");
        return timeTake;
    }


    @Override
    public String toString() {
        return "Order{" +
                "seq=" + seq +
                ", cust_seq='" + cust_seq + '\'' +
                ", seller_seq='" + seller_seq + '\'' +
                ", menu_seq='" + menu_seq + '\'' +
                ", num='" + num + '\'' +
                ", time_order='" + time_order + '\'' +
                ", time_take='" + time_take + '\'' +
                ", permit='" + permit + '\'' +
                '}';
    }
}