package com.sook.cs.letitgo.seller;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.sook.cs.letitgo.R;

public class seller_menu extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.seller_menu, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ListViewAdapter adapter = new ListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        ListView listview;
        listview = (ListView) getView().findViewById(R.id.listview);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, final int position, long id) {
                // item 가져오자
                final ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);

                String titleStr = item.getTitle(); //listItem 내용 읽기
                String descStr = item.getDesc();
                String priceStr = item.getPrice();
                Drawable iconDrawable = item.getIcon();

                // TODO : use item data.
                LayoutInflater inflater = getLayoutInflater(null);
                View alertLayout = inflater.inflate(R.layout.popup_activity, null);
                final EditText name = alertLayout.findViewById(R.id.name);
                final EditText details = alertLayout.findViewById(R.id.details);
                final EditText price = alertLayout.findViewById(R.id.price);
                final ImageView image = (ImageView) alertLayout.findViewById(R.id.img);
                final Button getPicture = (Button) alertLayout.findViewById(R.id.getPicture);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setView(alertLayout);
                alert.setCancelable(false); // disallow cancel of AlertDialog on click of back button and outside touch

                name.setText(titleStr);
                details.setText(descStr);
                price.setText(priceStr);
                image.setImageDrawable(iconDrawable);

                name.setFocusable(false);
                name.setClickable(false);
                details.setFocusable(false);
                details.setClickable(false);
                price.setFocusable(false);
                price.setClickable(false);
                getPicture.setEnabled(false);

                alert.setNeutralButton("삭제하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final AlertDialog.Builder check_alert = new AlertDialog.Builder(getActivity());
                        check_alert.setMessage("삭제하시겠습니까?")
                                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        adapter.deleteItem(position);
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(getActivity().getApplicationContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).create().show();
                    }
                });

                alert.setPositiveButton("수정하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                getPicture.setOnClickListener(new Button.OnClickListener() { //사진 불러오기
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity().getApplicationContext(), "사진을 불러와야 합니다", Toast.LENGTH_SHORT).show();
                    }
                });

                final AlertDialog dialog = alert.create();
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() { //수정하기
                    Boolean wantToCloseDialog = false;

                    @Override
                    public void onClick(View v) {
                        if (wantToCloseDialog) {//다이얼로그 닫기
                            dialog.dismiss();
                            String ed_name = name.getText().toString();
                            String ed_details = details.getText().toString();
                            String ed_price = price.getText().toString();
                            item.setTitle(ed_name);
                            item.setDesc(ed_details);
                            item.setPrice(ed_price);
                        } else { //다이얼로그 닫지 않고 EditText 수정 가능하게
                            name.setFocusableInTouchMode(true);
                            name.setClickable(true);
                            name.setFocusable(true);
                            details.setFocusableInTouchMode(true);
                            details.setClickable(true);
                            details.setFocusable(true);
                            price.setFocusableInTouchMode(true);
                            price.setClickable(true);
                            price.setFocusable(true);
                            getPicture.setEnabled(true);
                            wantToCloseDialog = true;
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setText("확인");
                        }
                    }
                });

            }
        });

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ아이템추가ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

        ImageView addItem = (ImageView) getView().findViewById(R.id.addListItem);
        addItem.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater(null);
                View alertLayout = inflater.inflate(R.layout.additem_popup_activity, null);

                final EditText name = alertLayout.findViewById(R.id.name);
                final EditText details = alertLayout.findViewById(R.id.details);
                final EditText price = alertLayout.findViewById(R.id.price);
                final ImageView image = (ImageView) alertLayout.findViewById(R.id.img);
                final Button getPicture = (Button) alertLayout.findViewById(R.id.getPicture);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("메뉴 추가하기");
                alert.setView(alertLayout);
                alert.setCancelable(false); // disallow cancel of AlertDialog on click of back button and outside touch

                alert.setPositiveButton("추가하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                final AlertDialog dialog = alert.create();
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() { //수정하기
                    Boolean wantToCloseDialog = false;

                    @Override
                    public void onClick(View v) {
                        //사진추가해야함.
                        String ed_name = name.getText().toString();
                        String ed_details = details.getText().toString();
                        String ed_price = price.getText().toString();

                        if (ed_name.equals("") || ed_details.equals("") || ed_price.equals("")) { //입력 다 안했음.
                            Toast.makeText(getActivity().getApplicationContext(), "입력들을 완성해주세요", Toast.LENGTH_SHORT).show();
                        }
                        else { //입력 완성시 다이얼로그 닫을 수 있게 허용.
                            wantToCloseDialog = true;
                            adapter.addItem(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.coffee),
                                    ed_name, ed_details, ed_price);
                            adapter.notifyDataSetChanged();
                        }
                        if (wantToCloseDialog) {//다이얼로그 닫기
                            dialog.dismiss();
                        }
                    }
                });

            }
        });

        adapter.addItem(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.coffee),
                "아메리카노", "설명", "1000원");

        adapter.addItem(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.coffee),
                "카페라테", "설명", "2000원");

        adapter.addItem(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.coffee),
                "카페모카", "설명", "3000원");

        adapter.addItem(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.coffee),
                "딸기스무디", "설명", "3000원");

        adapter.addItem(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.coffee),
                "베트남커피", "설명", "3000원");

        adapter.addItem(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.coffee),
                "아포가토", "설명", "3000원");

        adapter.addItem(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.coffee),
                "딸기생크림케이크", "설명", "3000원");
    }
}
