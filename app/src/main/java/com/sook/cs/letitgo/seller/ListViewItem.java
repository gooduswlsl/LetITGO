package com.sook.cs.letitgo.seller;

import android.graphics.drawable.Drawable;

public class ListViewItem { //하나의 listitem에 들어갈 요소들 만드는 class
    private Drawable iconDrawable ;
    private String titleStr ;
    private String descStr ;
    private String price;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }
    public void setPrice(String money) { price=money ; }
    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
    public String getPrice(){ return this.price; }
}