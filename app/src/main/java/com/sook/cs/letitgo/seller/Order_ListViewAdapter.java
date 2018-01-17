package com.sook.cs.letitgo.seller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.sook.cs.letitgo.R;

import java.util.ArrayList;

public class Order_ListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<Order_ListViewItem> listViewItemList = new ArrayList<Order_ListViewItem>();

    // ListViewAdapter의 생성자
    public Order_ListViewAdapter() {
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
        TextView textView2 = (TextView) convertView.findViewById(R.id.textView2);
        TextView textView3 = (TextView) convertView.findViewById(R.id.textView3);
        final Button btn_accept = (Button) convertView.findViewById(R.id.btn_accept);
        final Button btn_decline = (Button) convertView.findViewById(R.id.btn_decline);
        final TextView acceptStr = (TextView) convertView.findViewById(R.id.acceptStr);
        final TextView declineStr = (TextView) convertView.findViewById(R.id.declineStr);

        btn_accept.setTag(position);
        btn_decline.setTag(position);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt( (view.getTag().toString()) );
                btn_accept.setVisibility(View.GONE);
                btn_decline.setVisibility(View.GONE);
                acceptStr.setVisibility(View.VISIBLE);
                btn_decline.setClickable(false);
                final Order_ListViewItem item = (Order_ListViewItem)getItem(position);
                Toast.makeText(view.getContext(), item.getName()+"님의 주문을 수락하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt( (view.getTag().toString()) );//왜 추가했는지모름
                btn_accept.setVisibility(View.GONE);
                btn_decline.setVisibility(View.GONE);
                declineStr.setVisibility(View.VISIBLE);
                btn_accept.setClickable(false);
                final Order_ListViewItem item = (Order_ListViewItem)getItem(position);
                Toast.makeText(view.getContext(), item.getName()+"님의 주문을 거절하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Order_ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        textView1.setText(listViewItem.getName() + "(" + listViewItem.getId() + ") / " + listViewItem.getMenu());
        textView2.setText("희망수령시간: " + listViewItem.getRcv_time());

        textView3.setText("주문시간: " + listViewItem.getOrder_time() + "\n" +
                "가격: " + listViewItem.getPrice());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    //아이템추가
    public void addItem(String name, String id, String menu, String price, String rcv_time, String order_time) {
        Order_ListViewItem item = new Order_ListViewItem();

        item.setName(name);
        item.setId(id);
        item.setMenu(menu);
        item.setPrice(price);
        item.setRcv_time(rcv_time);
        item.setOrder_time(order_time);

        listViewItemList.add(item);
    }

    //아이템삭제
    public void deleteItem(int position) {
        listViewItemList.remove(position);
    }

}
