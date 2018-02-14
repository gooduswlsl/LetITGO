package com.sook.cs.letitgo.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Sales implements Serializable {
    public int month;
    public int sales;

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    public int getSales() { return sales; }
    public void setSales(int total) { this.sales = total; }

    @Override
    public String toString() {
        return "Sales{" +
                "month=" + month+
                " total=" + sales+
                '}';
    }
}