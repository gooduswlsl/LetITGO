package com.sook.cs.letitgo.seller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sook.cs.letitgo.MyApp;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.lib.StringLib;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class seller_menu extends Fragment {

    private static final int ADD_MENU = 1;
    private static final int EDIT_MENU = 2;
    private static final int DELETE_MENU = 3;

    private static final int EDIT_PICTURE_MENU =1;
    private static final int ADD_PICTURE_MENU=2;

    public Menu item;
    public ImageView image;
    public ImageView image2;
    private String add_img_name;
    private TextView first_visit;

    private final String TAG = this.getClass().getSimpleName();
    Menu menu_item;

    Context context;
    int memberSeq;
    Seller current_seller;

    ListViewAdapter adapter;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == EDIT_PICTURE_MENU){
                add_img_name= data.getStringExtra("filename")+".png";
                Picasso.with(getActivity().getApplicationContext())
                        .load(RemoteService. MENU_IMG_URL +  add_img_name)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(image);
            }

            if(requestCode == ADD_PICTURE_MENU){
                add_img_name= data.getStringExtra("filename")+".png";
                Picasso.with(getActivity().getApplicationContext())
                        .load(RemoteService. MENU_IMG_URL +  add_img_name)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(image2);
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(),"사진등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getActivity();

        current_seller = ((MyApp) getActivity().getApplicationContext()).getSeller();  //현재 seller정보 가져오기
        memberSeq = current_seller.getSeq();
        return inflater.inflate(R.layout.seller_menu, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new ListViewAdapter();

        ListView listview;
        listview = (ListView) getView().findViewById(R.id.listview);
        listview.setAdapter(adapter);
        first_visit = (TextView) getView().findViewById(R.id.first_visit);
        showMenuList(memberSeq);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, final int position, long id) {
                // 클릭한 item 가져오기
                item = (Menu) parent.getItemAtPosition(position); //final에서 바꿔줌

                final String titleStr = item.getmName();
                String descStr = item.getmDetail();
                String priceStr = String.valueOf(item.getmPrice());

                // TODO : use item data.
                LayoutInflater inflater = getLayoutInflater(null);
                View alertLayout = inflater.inflate(R.layout.additem_popup_activity, null);
                final EditText name = alertLayout.findViewById(R.id.name);
                final EditText details = alertLayout.findViewById(R.id.details);
                final EditText price = alertLayout.findViewById(R.id.price);
                image = (ImageView) alertLayout.findViewById(R.id.img);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setView(alertLayout);
                alert.setCancelable(false);

                name.setText(titleStr);
                details.setText(descStr);
                price.setText(priceStr);

                changeImage();

                name.setFocusable(false);
                name.setClickable(false);
                details.setFocusable(false);
                details.setClickable(false);
                price.setFocusable(false);
                price.setClickable(false);

                alert.setNeutralButton("삭제하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        menu_item = new Menu();

                        final AlertDialog.Builder check_alert = new AlertDialog.Builder(getActivity());
                        check_alert.setMessage("이 메뉴를 삭제하시겠습니까?")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        adapter.deleteItem(position);
                                        adapter.notifyDataSetChanged();
                                        menu_item.mName = titleStr;
                                        menu_item.action = DELETE_MENU;
                                        insertMenuInfo();

                                        Toast.makeText(getActivity().getApplicationContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
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

                alert.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                image.setOnClickListener(new Button.OnClickListener() { //사진 불러오기
                    @Override
                    public void onClick(View view) { //메뉴 수정(사진찍기)

                        Intent intent = new Intent(getActivity().getApplicationContext(), MenuPicEditActivity.class);
                        intent.putExtra("item",item);

                        startActivityForResult(intent, EDIT_PICTURE_MENU);

                    }
                });

                final AlertDialog dialog = alert.create();
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() { //수정하기
                    Boolean wantToCloseDialog = false;

                    @Override
                    public void onClick(View v) {
                        if (wantToCloseDialog) {//다이얼로그 닫기
                            menu_item = new Menu();
                            dialog.dismiss();
                            String ed_name = name.getText().toString();
                            String ed_details = details.getText().toString();
                            String ed_price = price.getText().toString();

                            menu_item.mSeq=item.getmSeq();
                            menu_item.mName = ed_name;
                            menu_item.mDetail = ed_details;
                            menu_item.mPrice = Integer.parseInt(ed_price);
                            menu_item.mImgUrl=add_img_name;
                            menu_item.action = EDIT_MENU;

                            insertMenuInfo();

                            item.setmName(ed_name);
                            item.setmDetail(ed_details);
                            item.setmPrice(Integer.parseInt(ed_price));

                            //adapter 다시 불러오기
                            adapter = new ListViewAdapter();
                            ListView listview;
                            listview = (ListView) getView().findViewById(R.id.listview);
                            listview.setAdapter(adapter);
                            showMenuList(memberSeq);

                            adapter.notifyDataSetChanged();

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
                image2 = (ImageView) alertLayout.findViewById(R.id.img);

                image2.setOnClickListener( //메뉴 추가(사진찍기)
                        new Button.OnClickListener() {
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity().getApplicationContext(), MenuPicAddActivity.class);
                                startActivityForResult(intent, ADD_PICTURE_MENU);
                            }
                        }
                );

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setView(alertLayout);
                alert.setCancelable(false);

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

                        menu_item = new Menu();

                        //사진추가해야함.
                        String ed_name = name.getText().toString();
                        String ed_details = details.getText().toString();
                        String ed_price = price.getText().toString();

                        if (ed_name.equals("") || ed_details.equals("") || ed_price.equals("")) {
                            Toast.makeText(getActivity().getApplicationContext(), "입력들을 완성해주세요", Toast.LENGTH_SHORT).show();
                        } else { //입력 완성시 다이얼로그 닫을 수 있게 허용.
                            wantToCloseDialog = true;
                            adapter.notifyDataSetChanged();
                        }
                        if (wantToCloseDialog) {//다이얼로그 닫기
                            Log.d(TAG,menu_item.toString());
                            menu_item.mName = ed_name;
                            menu_item.mDetail = ed_details;
                            menu_item.mPrice = Integer.parseInt(ed_price);
                            menu_item.seller_seq = memberSeq;
                            menu_item.mImgUrl = add_img_name;
                            menu_item.action = ADD_MENU;
                            insertMenuInfo();
                            showMenuList(memberSeq);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    // 메뉴 수정, 추가, 삭제시 서버에 저장
    private void insertMenuInfo() {
        Log.d(TAG, menu_item.toString());

        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.insertMenuInfo(menu_item);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    int seq = 0;
                    String seqString = response.body();

                    try {
                        seq = Integer.parseInt(seqString);
                    } catch (Exception e) {
                        seq = 0;
                    }

                    if (seq == 0) {
                        //등록 실패
                    } else {
//                        infoItem.seq = seq;
                    }
                } else { // 등록 실패
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    Log.d(TAG, "fail " + statusCode + errorBody.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "no internet connectivity");
            }
        });
    }

    // DB에서 memberSeq에 해당하는 메뉴내용 가져오기
    private void showMenuList(int memberSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<Menu>> call
                = remoteService.listMenu(memberSeq);
        call.enqueue(new Callback<ArrayList<Menu>>() {

            @Override
            public void onResponse(Call<ArrayList<Menu>> call,
                                   Response<ArrayList<Menu>> response) {
                ArrayList<Menu> list = response.body();

                if (list == null) {
                    list = new ArrayList<>();
                }

                if (response.isSuccessful()) {
                    Log.d(TAG, "list size " + list.size());
                    if (list.size() == 0) {

                    } else {
                        first_visit.setVisibility(View.GONE);
                        adapter.setItemList(list);
                    }
                } else {
                    Log.d(TAG, "not success");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Menu>> call, Throwable t) {
                Log.d(TAG, "no internet connectivity");
                Log.d(TAG, t.toString());
            }
        });
    }

    public void changeImage(){
        if (StringLib.getInstance().isBlank(item.mImgUrl)) {
            Picasso.with(getActivity().getApplicationContext()).load(R.drawable.noimage).into(image);
        } else {
            Picasso.with(getActivity().getApplicationContext())
                    .load(RemoteService. MENU_IMG_URL + item.mImgUrl)
                    .into(image);
        }

    }

}

