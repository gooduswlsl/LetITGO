package com.sook.cs.letitgo.seller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                seller_store_info tab1 = new seller_store_info();
                return tab1;
            case 1:
               seller_order tab2 = new seller_order();
                return tab2;
            case 2:
                seller_menu tab3 = new seller_menu();
                return tab3;
            case 3:
                seller_sales tab4 = new seller_sales();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
