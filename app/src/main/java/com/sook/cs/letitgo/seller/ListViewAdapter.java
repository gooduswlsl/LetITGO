package com.sook.cs.letitgo.seller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.lib.StringLib;
import com.sook.cs.letitgo.remote.RemoteService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
//    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;
    private ArrayList<Menu> listViewItemList = new ArrayList<Menu>() ;
    // ListViewAdapter의 생성자
    public ListViewAdapter() {
    }

    public void updateAdapter( ArrayList<Menu> listViewItemList){ //추가했음
        this.listViewItemList=listViewItemList;
        notifyDataSetChanged();
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    public void setItemList(ArrayList<Menu> itemList) {
        this.listViewItemList = itemList;
        notifyDataSetChanged();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Menu listViewItem = listViewItemList.get(position);


        // 아이템 내 각 위젯에 데이터 반영
        if (StringLib.getInstance().isBlank(listViewItem.mImgUrl)) {
            Picasso.with(context).load(R.drawable.noimage).into(iconImageView);
        } else {
            Picasso.with(context)
                    .load(RemoteService.MENU_IMG_URL + listViewItem.mImgUrl)
                    .into(iconImageView);
        }
        titleTextView.setText(listViewItem.getmName());
        if(listViewItem.getmDetail()==null){
            descTextView.setText(" ("+String.valueOf(listViewItem.getmPrice())+"원)");
        }
        else
            descTextView.setText(listViewItem.getmDetail()+" ("+String.valueOf(listViewItem.getmPrice())+"원)");

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }


    //아이템삭제
    public void deleteItem(int position){
        listViewItemList.remove(position);
    }
}