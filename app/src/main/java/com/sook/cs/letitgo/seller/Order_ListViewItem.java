package com.sook.cs.letitgo.seller;

public class Order_ListViewItem { //하나의 listitem에 들어갈 요소들 만드는 class
    private String name;
    private String id;
    private String menu;
    private String price;
    private String rcv_time;
    private String order_time;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMenu() { return menu; }
    public void setMenu(String menu) { this.menu = menu; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    public String getRcv_time() { return rcv_time; }
    public void setRcv_time(String rcv_time) { this.rcv_time = rcv_time; }
    public String getOrder_time() { return order_time; }
    public void setOrder_time(String order_time) { this.order_time = order_time; }

}