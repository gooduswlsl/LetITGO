package com.sook.cs.letitgo.seller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.sook.cs.letitgo.R;

public class seller_order extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.seller_order, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Order_ListViewAdapter adapter = new Order_ListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        ListView listview;
        listview = (ListView) getView().findViewById(R.id.listview);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, final int position, long id) {
                // item 가져오자
                final Order_ListViewItem item = (Order_ListViewItem) parent.getItemAtPosition(position);

                String nameStr = item.getName(); //listItem 내용 읽기
                String idStr = item.getId();
                String priceStr = item.getPrice();
                String rcv_timeStr = item.getRcv_time();
                String order_timeStr = item.getOrder_time();

                // TODO : use item data.
            };
        });

        adapter.addItem("김예원","yaewon95","초코스무디","4400원","오늘 오후 2:13","2018-01-13 1:30");
        adapter.addItem("정재원","재워니","아이스아메리카노","4100원","오늘 오후 2:13","2018-01-13 1:30");
        adapter.addItem("홍연진","연지니","아포카토","5000원","오늘 오후 2:13","2018-01-13 1:30");
        adapter.addItem("홍연진","연지니","아포카토","5000원","오늘 오후 2:13","2018-01-13 1:30");
        adapter.addItem("홍연진","연지니","아포카토","5000원","오늘 오후 2:13","2018-01-13 1:30");
        adapter.addItem("홍연진","연지니","아포카토","5000원","오늘 오후 2:13","2018-01-13 1:30");
        adapter.addItem("홍연진","연지니","아포카토","5000원","오늘 오후 2:13","2018-01-13 1:30");
    }
}
