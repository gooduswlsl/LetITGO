package com.sook.cs.letitgo.util;

import com.sook.cs.letitgo.Menu;
import com.sook.cs.letitgo.Store;

import java.util.ArrayList;

public class DataUtil {

    public static ArrayList<Store> getStoreArrayList() {
        ArrayList<Store> storeList = new ArrayList<>();
        storeList.add(new Store("할리스", "숙대점", "010-4536-9508", "www.naver.com", "서울특별시 용산구", "https://pbs.twimg.com/profile_images/1570768836/phpPj5DaU"));
        storeList.add(new Store("스타벅스", "죽전점", "010-4536-9508",
                "www.daum.net", "경기도 용인시 수지구", "http://logok.org/wp-content/uploads/2014/08/Starbucks-logo-2000px-png.png"));
        return storeList;
    }

    public static ArrayList<Menu> getMenuArrayList() {
        ArrayList<Menu> menuList = new ArrayList<>();
        menuList.add(new Menu("아메리카노", "http://www.rotiboy.kr/uploads/menu/Dio548fuq13CkrX6.png", 5000, "맛있어요", "할리스", "숙대점"));
        menuList.add(new Menu("카페라떼", "http://www.dunkindonuts.co.kr/upload/product/big_824.png", 5500, "맛나요", "스타벅스", "죽전점"));
        return menuList;
    }
}