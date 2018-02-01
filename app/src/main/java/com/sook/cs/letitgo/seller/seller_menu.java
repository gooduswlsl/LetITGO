package com.sook.cs.letitgo.seller;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class seller_menu extends Fragment {

    private static final int ADD_MENU = 1;
    private static final int EDIT_MENU = 2;
    private static final int DELETE_MENU = 3;

    private final String TAG = this.getClass().getSimpleName();
    Menu menu_item;

    Context context;
    int memberSeq;

    ListViewAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getActivity();

//        memberSeq = ((MyApp)getActivity().getApplication()). getSeller_seq();
        memberSeq = 1;  //memberSeq=1라고 가정
        return inflater.inflate(R.layout.seller_menu, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new ListViewAdapter();

        ListView listview;
        listview = (ListView) getView().findViewById(R.id.listview);
        listview.setAdapter(adapter);
        showMenuList(memberSeq);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, final int position, long id) {
                // 클릭한 item 가져오기
                final Menu item = (Menu) parent.getItemAtPosition(position);

                final String titleStr = item.getmName();
                String descStr = item.getmDetail();
                String priceStr = String.valueOf(item.getmPrice());
//                Drawable iconDrawable = item.getIcon();

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
                alert.setCancelable(false);

                name.setText(titleStr);
                details.setText(descStr);
                price.setText(priceStr);
//                image.setImageDrawable(iconDrawable);

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

                        menu_item = new Menu();

                        final AlertDialog.Builder check_alert = new AlertDialog.Builder(getActivity());
                        check_alert.setMessage("삭제하시겠습니까?")
                                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
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
                            menu_item = new Menu();
                            dialog.dismiss();
                            String ed_name = name.getText().toString();
                            String ed_details = details.getText().toString();
                            String ed_price = price.getText().toString();

                            menu_item.mSeq=item.getmSeq();
                            menu_item.mName = ed_name;
                            menu_item.mDetail = ed_details;
                            menu_item.mPrice = Integer.parseInt(ed_price);
                            menu_item.action = EDIT_MENU;

                            insertMenuInfo();

                            item.setmName(ed_name);
                            item.setmDetail(ed_details);
                            item.setmPrice(Integer.parseInt(ed_price));

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
                            adapter.addItem(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.coffee),
                                    ed_name, ed_details, ed_price);
                            adapter.notifyDataSetChanged();
                        }
                        if (wantToCloseDialog) {//다이얼로그 닫기
                            menu_item.mName = ed_name;
                            menu_item.mDetail = ed_details;
                            menu_item.mPrice = Integer.parseInt(ed_price);
//                            menu_item.seller_seq = memberSeq;
                            menu_item.seller_seq = 1; //라고 가정
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
}
