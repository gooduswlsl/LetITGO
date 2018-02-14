package com.sook.cs.letitgo.seller;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.item.Customer;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.item.Order;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Order_ListViewAdapter extends BaseAdapter {

    private final String TAG = this.getClass().getSimpleName();
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    public ArrayList<Order> listViewItemList = new ArrayList<Order>();
    public ArrayList<Customer> c_list = new ArrayList<Customer>();
    public ArrayList<Menu> m_list = new ArrayList<Menu>();

    private static final int ACCEPT_ORDER = 1;
    private static final int DECLINE_ORDER = -1;
    private static final int ORDER_FINISHED = 2;

    // Order_ListViewAdapter의 생성자
    public Order_ListViewAdapter() {
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        // "order_listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        final TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
        final TextView textView2 = (TextView) convertView.findViewById(R.id.textView2);
        final TextView textView3 = (TextView) convertView.findViewById(R.id.textView3);
        final Button btn_accept = (Button) convertView.findViewById(R.id.btn_accept);
        final Button btn_decline = (Button) convertView.findViewById(R.id.btn_decline);
        final Button btn_finished = (Button) convertView.findViewById(R.id.btn_finished);
        final TextView acceptStr = (TextView) convertView.findViewById(R.id.acceptStr);
        final TextView declineStr = (TextView) convertView.findViewById(R.id.declineStr);


//        btn_accept.setTag(position);
//        btn_decline.setTag(position);

        btn_accept.setOnClickListener(new View.OnClickListener() {  //수락

            @Override
            public void onClick(final View view) {
                btn_accept.setVisibility(View.GONE);
                btn_decline.setVisibility(View.GONE);
                btn_decline.setClickable(false);
                btn_finished.setVisibility(View.VISIBLE);
                btn_finished.setClickable(true);
                sendPermit(listViewItemList.get(position).getSeq(),ACCEPT_ORDER, position);
                sendTotal_price(listViewItemList.get(position).getSeq(),m_list.get(position).getmPrice()*listViewItemList.get(position).getNum());

                Toast.makeText(view.getContext(), c_list.get(position).getName()+"님의 주문을 수락하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_decline.setOnClickListener(new View.OnClickListener() { //거절
            @Override
            public void onClick(final View view) {
                btn_accept.setVisibility(View.GONE);
                btn_decline.setVisibility(View.GONE);
                declineStr.setVisibility(View.VISIBLE);
                btn_accept.setClickable(false);
                sendPermit(listViewItemList.get(position).getSeq(),DECLINE_ORDER, position);

                Toast.makeText(view.getContext(), c_list.get(position).getName()+"님의 주문을 거절하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_finished.setOnClickListener(new View.OnClickListener() {  //주문완료

            @Override
            public void onClick(final View view) {
                btn_finished.setVisibility(View.GONE);
                acceptStr.setVisibility(View.VISIBLE);
                sendPermit(listViewItemList.get(position).getSeq(),ORDER_FINISHED, position);

                Toast.makeText(view.getContext(), c_list.get(position).getName()+"님의 주문을 완료하였습니다.", Toast.LENGTH_SHORT).show();

            }
        });

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final Order listViewItem = listViewItemList.get(position);

        //해당 주문과 관련된 customer, menu 정보 가져오기
        findCustomerInfo(listViewItem.getCust_seq());
        selectMenuInfo(listViewItem.getMenu_seq());

        //서버에서 database를 가져오는 시간 때문에 1초의 시간 지연을 주었다.
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //예약시간String 깔끔하게
                String all=listViewItem.getTime_take();
                int here= all.indexOf("T");
                String date = all.substring(0, here);
                String time = all.substring(here+1,here+6);

                // 아이템 내 각 위젯에 데이터 반영
                textView1.setText(c_list.get(position).getName()+" (" + c_list.get(position).getPhone()+") ");
                textView2.setText("주문 메뉴: "+m_list.get(position).getmName()+" "+listViewItem.getNum()+"개");
                textView3.setText("예약 날짜: "+date+"\n"+"예약 시간: "+time);


                switch(listViewItem.getPermit()){
                    case DECLINE_ORDER:
                        btn_accept.setVisibility(View.GONE);
                        btn_decline.setVisibility(View.GONE);
                        declineStr.setVisibility(View.VISIBLE);
                        break;

                    case ACCEPT_ORDER:
                        btn_accept.setVisibility(View.GONE);
                        btn_decline.setVisibility(View.GONE);
                        btn_finished.setVisibility(View.VISIBLE);
                        break;

                    case ORDER_FINISHED:
                        btn_accept.setVisibility(View.GONE);
                        btn_decline.setVisibility(View.GONE);
                        acceptStr.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }

            }
        }, 1000);


        return convertView;

    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    public Customer getPositionCustomer(int position){
        return c_list.get(position);
    }

    public Menu getPositionMenu(int position) {
        return m_list.get(position);
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    public void setItemList(ArrayList<Order> itemList) {
        this.listViewItemList = itemList;
        notifyDataSetChanged();
    }

    private void findCustomerInfo(int seq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<Customer> call = remoteService. selectCustomerInfo(seq);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    c_list.add(response.body());
                } else {
                    Log.d("ok", "findCustomerInfo unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.d("ok", "failure");
            }
        });
    }

    private void selectMenuInfo(final int menu_seq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<Menu> call = remoteService.selectMenu(menu_seq);
        call.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                if (response.isSuccessful()) {
                    m_list.add(response.body());
                } else {
                    Log.d("ok", "selectMenuInfo unsuccessful");
                }
            }
            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
            }
        });
    }

    // permit값 보내기
    private void sendPermit(int seq, int permit, int position) {
        final int seller_seq=listViewItemList.get(position).getSeller_seq();
        Log.d(TAG,"sendPermit seller_seq: "+seller_seq);
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.sendPermit(permit,seq);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    showOrderList(seller_seq);
                    Log.d(TAG,"sendPermit successful");
                }
                else{
                    Log.d(TAG,"response not successful");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "no internet connectivity");
                Log.d(TAG, t.toString());
            }
        });
    }

    // db에 수락한 주문의 total_price 올리기 (order_sales의 편리성을 위해)
    private void sendTotal_price(int seq, int total_price) {
        Log.d(TAG,"sendTotal_price: "+total_price);
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.sendTotal_price(seq, total_price);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG,"sendPermit successful");
                }
                else{
                    Log.d(TAG,"response not successful");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "no internet connectivity");
                Log.d(TAG, t.toString());
            }
        });
    }

    // DB에서 memberSeq에 해당하는 메뉴내용 가져오기
    private void showOrderList(int memberSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<Order>> call
                = remoteService.listOrder(memberSeq);
        call.enqueue(new Callback<ArrayList<Order>>() {

            @Override
            public void onResponse(Call<ArrayList<Order>> call,
                                   Response<ArrayList<Order>> response) {
                ArrayList<Order> list = response.body();

                if (list == null) {
                    list = new ArrayList<>();
                }

                if (response.isSuccessful()) {
                    Log.d(TAG, "list size " + list.size());
                    if (list.size() == 0) {

                    } else {
                        setItemList(list);
                    }
                } else {
                    Log.d(TAG, "not success");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                Log.d(TAG, "no internet connectivity");
                Log.d(TAG, t.toString());
            }
        });

    }

}
