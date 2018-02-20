package com.sook.cs.letitgo.customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sook.cs.letitgo.MyApp;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.item.Customer;
import com.sook.cs.letitgo.lib.StringLib;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_my extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private ImageView profile_icon, profile_icon_change;
    private TextView profile_name, profile_sextype, profile_birth, profile_phone, leaveMember;
    private Button changeProfile, changeProfile2;
    Customer current_customer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_my, container, false);

        current_customer = ((MyApp) getActivity().getApplicationContext()).getCustomer();

        profile_icon=layout.findViewById(R.id.profile_icon);
        profile_icon_change=layout.findViewById(R.id.profile_icon_change);
        profile_name=layout.findViewById(R.id.profile_name);
        profile_sextype=layout.findViewById(R.id.profile_sextype);
        profile_birth=layout.findViewById(R.id.profile_birth);
        profile_phone=layout.findViewById(R.id.profile_phone);
        changeProfile=layout.findViewById(R.id.changeProfile);
        changeProfile2=layout.findViewById(R.id.changeProfile2);
        leaveMember=layout.findViewById(R.id.leaveMember);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showPicture();

        profile_name.setText(current_customer.name);
        profile_sextype.setText(current_customer.sextype);
        profile_birth.setText(current_customer.birthday);
        profile_phone.setText(current_customer.phone);
        profile_icon.setOnClickListener(new View.OnClickListener() {  //회원정보 수정
            @Override
            public void onClick(final View view) {
                startProfileIconChange();
            }
        });

        profile_icon_change.setOnClickListener(new View.OnClickListener() {  //회원정보 수정
            @Override
            public void onClick(final View view) {
                startProfileIconChange();
            }
        });

      changeProfile.setOnClickListener(new View.OnClickListener() {  //회원정보 수정
            @Override
            public void onClick(final View view) {
                profile_icon.setEnabled(true);
                profile_name.setEnabled(true);
                profile_birth.setEnabled(true);
                profile_sextype.setEnabled(true);
                changeProfile.setVisibility(View.GONE);
                profile_icon_change.setVisibility(View.VISIBLE);
                changeProfile2.setVisibility(View.VISIBLE);
            }
        });

        changeProfile2.setOnClickListener(new View.OnClickListener() {  //회원정보 수정완료
            @Override
            public void onClick(final View view) {
                current_customer.setName(profile_name.getText().toString());
                current_customer.setBirthday(profile_birth.getText().toString());
                current_customer.setSextype(profile_sextype.getText().toString());
                changeCustomerInfo();
            }
        });

        leaveMember.setOnClickListener(new View.OnClickListener() {  //회원탈퇴
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
                alert_confirm.setMessage("정말 탈퇴하시겠습니까?").setCancelable(false).setPositiveButton("확인",

                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { //확인
                                leaveMember();
                            }

                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {

                           @Override
                           public void onClick(DialogInterface dialog, int which) { //취소
                             return;
                           }
                        });
                        AlertDialog alert = alert_confirm.create();
                        alert.show();
            }
        });
    }


    // 회원정보 수정(서버 전송)
    private void changeCustomerInfo() {
        Log.d(TAG, current_customer.toString());
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.changeCustomerInfo(current_customer);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) { //수정 성공
                    Toast.makeText(getContext(), "회원정보 수정완료", Toast.LENGTH_SHORT).show();
                    refresh();
                } else { // 수정 실패
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    Log.d(TAG, "fail " + statusCode + errorBody.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "no internet connectivity");
                Log.d(TAG, t.toString());
            }
        });
    }

    // 회원 탈퇴
    private void leaveMember() {
        Log.d(TAG, current_customer.toString());
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.leaveMember(current_customer.getSeq());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent( getContext(), customer_leave.class);
                    startActivity(intent);

                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    Log.d(TAG, "fail " + statusCode + errorBody.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "no internet connectivity");
                Log.d(TAG, t.toString());
            }
        });
    }

    //화면 재시작
    private void refresh() {
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            current_customer.img = data.getStringExtra("imgname");
            showPicture();
    }

    private void startProfileIconChange() {
        Intent it = new Intent(getContext(), customer_changePic.class);
        startActivityForResult(it,100);
    }

    private void showPicture(){
        if (StringLib.getInstance().isBlank(current_customer.img)) {
            Picasso.with(getActivity().getApplicationContext()).load(R.drawable.ic_person).into(profile_icon);
        } else {
            Picasso.with(getActivity().getApplicationContext())
                    .load(RemoteService.CUSTOMER_IMG_URL + current_customer.img)
                    .into(profile_icon);
        }
    }
}
