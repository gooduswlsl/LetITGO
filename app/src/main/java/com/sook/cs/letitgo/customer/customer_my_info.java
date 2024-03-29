package com.sook.cs.letitgo.customer;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.Calendar;
import java.util.GregorianCalendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_my_info extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private ImageView profile_icon, profile_icon_change;
    private EditText profile_name, profile_sextype, profile_birth, profile_phone;
    private TextView leaveMember;
    private Button changeProfile;
    Customer current_customer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_my_info, container, false);

        current_customer = ((MyApp) getActivity().getApplicationContext()).getCustomer();

        profile_icon = layout.findViewById(R.id.profile_icon);
        profile_icon_change = layout.findViewById(R.id.profile_icon_change);
        profile_name = layout.findViewById(R.id.profile_name);
        profile_sextype = layout.findViewById(R.id.profile_sextype);
        profile_birth = layout.findViewById(R.id.profile_birth);
        profile_phone = layout.findViewById(R.id.profile_phone);
        changeProfile = layout.findViewById(R.id.changeProfile);
        leaveMember = layout.findViewById(R.id.leaveMember);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showPicture();

        profile_name.setText(current_customer.name);
        profile_sextype.setText(current_customer.sextype);
        profile_sextype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSexTypeDialog();
            }
        });
        profile_birth.setText(current_customer.birthday);
        profile_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBirthdayDialog();
            }
        });
        profile_phone.setText(current_customer.phone);

        profile_icon_change.setOnClickListener(new View.OnClickListener() {  //회원정보 수정
            @Override
            public void onClick(final View view) {
                startProfileIconChange();
            }
        });

        changeProfile.setOnClickListener(new View.OnClickListener() {  //회원정보 수정
            @Override
            public void onClick(final View view) {
                if (changeProfile.getText().toString().equals("수정하기")) {
                    profile_icon.setEnabled(true);
                    profile_name.setEnabled(true);
                    profile_birth.setEnabled(true);
                    profile_sextype.setEnabled(true);
                    changeProfile.setText("수정완료");
                    profile_icon_change.setVisibility(View.VISIBLE);
                } else {
                    current_customer.setName(profile_name.getText().toString());
                    current_customer.setBirthday(profile_birth.getText().toString());
                    current_customer.setSextype(profile_sextype.getText().toString());
                    changeCustomerInfo();
                }
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
                    Intent intent = new Intent(getContext(), customer_leave.class);
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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
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
        startActivityForResult(it, 100);
    }

    private void showPicture() {
        if (StringLib.getInstance().isBlank(current_customer.img)) {
            Picasso.with(getActivity().getApplicationContext()).load(R.drawable.ic_person).into(profile_icon);
        } else {
            Picasso.with(getActivity().getApplicationContext())
                    .load(RemoteService.CUSTOMER_IMG_URL + current_customer.img)
                    .into(profile_icon);
        }
    }

    /**
     * 성별을 선택할 수 있는 다이얼로그를 보여준다.
     */
    private void setSexTypeDialog() {
        final String[] sexTypes = new String[2];
        sexTypes[0] = getResources().getString(R.string.sex_woman);
        sexTypes[1] = getResources().getString(R.string.sex_man);

        new AlertDialog.Builder(getActivity())
                .setItems(sexTypes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which >= 0) {
                            profile_sextype.setText(sexTypes[which]);
                            current_customer.sextype=sexTypes[which];
                        }
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 생일을 선택할 수 있는 다이얼로그를 보여준다.
     */
    private void setBirthdayDialog() {
        GregorianCalendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String myMonth;
                if (monthOfYear + 1 < 10) {
                    myMonth = "0" + (monthOfYear + 1);
                } else {
                    myMonth = "" + (monthOfYear + 1);
                }

                String myDay;
                if (dayOfMonth < 10) {
                    myDay = "0" + dayOfMonth;
                } else {
                    myDay = "" + dayOfMonth;
                }

                String date = year + " " + myMonth + " " + myDay;
                profile_birth.setText(date);
                current_customer.birthday=date;
            }
        }, year, month, day).show();
    }

}
