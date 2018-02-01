package com.sook.cs.letitgo.shared;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sook.cs.letitgo.MyApp;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.customer.customer_main;
import com.sook.cs.letitgo.item.Customer;
//import com.sook.cs.letitgo.item.Member;
import com.sook.cs.letitgo.lib.EtcLib;
import com.sook.cs.letitgo.lib.StringLib;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 프로필을 설정할 수 있는 액티비티
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;

    ImageView profileIconImage;
    ImageView profileIconChangeImage;
    EditText nameEdit;
    EditText sextypeEdit;
    EditText birthEdit;
    EditText phoneEdit;

    Customer currentItem;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if(requestCode==0)
          //  if(resultCode==1)
                currentItem.img=data.getStringExtra("img_name");
    }

    /**
     * 액티비티를 생성하고 화면을 구성한다.
     *
     * @param savedInstanceState 액티비티가 새로 생성되었을 경우, 이전 상태 값을 가지는 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        currentItem = ((MyApp) getApplicationContext()).getCustomer();
        context = this;
        setView();
    }

    /**
     * 화면이 보여질 때 호출되며 사용자 정보를 기반으로 프로필 아이콘을 설정한다.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (StringLib.getInstance().isBlank(currentItem.img)) {
            Picasso.with(this).load(R.drawable.ic_person).into(profileIconImage);
        } else {
            Picasso.with(this)
                    .load(RemoteService.MEMBER_ICON_URL + currentItem.img)
                    .into(profileIconImage);
        }
    }

    /**
     * 액티비티 화면을 설정한다.
     */
    private void setView() {
        profileIconImage = (ImageView) findViewById(R.id.profile_icon);
        profileIconImage.setOnClickListener(this);

        profileIconChangeImage = (ImageView) findViewById(R.id.profile_icon_change);
        profileIconChangeImage.setOnClickListener(this);

        nameEdit = (EditText) findViewById(R.id.profile_name);

        sextypeEdit = (EditText) findViewById(R.id.profile_sextype);
        sextypeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSexTypeDialog();
            }
        });

        birthEdit = (EditText) findViewById(R.id.profile_birth);
        birthEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBirthdayDialog();
            }
        });

        String phoneNumber = EtcLib.getInstance().getPhoneNumber(context);

        phoneEdit = (EditText) findViewById(R.id.profile_phone);
        phoneEdit.setText(phoneNumber);

        TextView phoneStateEdit = (TextView) findViewById(R.id.phone_state);
        if (phoneNumber.startsWith("0")) {
            phoneStateEdit.setText("(" + getResources().getString(R.string.device_number) + ")");
        } else {
            phoneStateEdit.setText("(" + getResources().getString(R.string.phone_number) + ")");
        }
    }

    /**
     * 성별을 선택할 수 있는 다이얼로그를 보여준다.
     */
    private void setSexTypeDialog() {
        final String[] sexTypes = new String[2];
        sexTypes[0] = getResources().getString(R.string.sex_woman);
        sexTypes[1] = getResources().getString(R.string.sex_man);

        new AlertDialog.Builder(this)
                .setItems(sexTypes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which >= 0) {
                            sextypeEdit.setText(sexTypes[which]);
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

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
                birthEdit.setText(date);
            }
        }, year, month, day).show();
    }

    /**
     * 오른쪽 상단 메뉴를 구성한다.
     *
     * @param menu 메뉴 객체
     * @return 메뉴를 보여준다면 true, 보여주지 않는다면 false
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_profile, menu);
        return true;
    }

    /**
     * 오른쪽 상단 확인 메뉴를 클릭했을 때의 동작을 지정한다.
     * 여기서는 모든 버튼이 액티비티를 종료한다.
     *
     * @param item 메뉴 아이템 객체
     * @return 메뉴를 처리했다면 true, 그렇지 않다면 false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                save();
                break;
        }

        return true;
    }

    /**
     * 사용자가 입력한 정보를 MemberInfoItem 객체에 저장해서 반환한다.
     *
     * @return 사용자 정보 객체
     */
    private Customer getCustomerInfo() {
        Customer newItem = new Customer();
        newItem.phone = EtcLib.getInstance().getPhoneNumber(context);
        newItem.name = nameEdit.getText().toString();
        newItem.sextype = sextypeEdit.getText().toString();
        newItem.birthday = birthEdit.getText().toString().replace(" ", "");
        newItem.img = currentItem.img;

        return newItem;
    }

    /**
     * 기존 사용자 정보와 새로 입력한 사용자 정보를 비교해서 변경되었는지를 파악한다.
     *
     * @param newItem 사용자 정보 객체
     * @return 변경되었다면 true, 변경되지 않았다면 false
     */
    private boolean isChanged(Customer newItem) {
        if (newItem.name.trim().equals(currentItem.name)
                && newItem.sextype.trim().equals(currentItem.sextype)
                && newItem.birthday.trim().equals(currentItem.birthday)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 사용자가 이름을 입력했는지를 확인한다.
     *
     * @param newItem 사용자가 새로 입력한 정보 객체
     * @return 입력하지 않았다면 true, 입력했다면 false
     */
    private boolean isNoName(Customer newItem) {
        if (StringLib.getInstance().isBlank(newItem.name)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 사용자가 입력한 정보를 저장한다.
     */
    private void save() {
        Log.d("ok", "save");
        Log.d("ok", currentItem.img);
        final Customer newItem = getCustomerInfo();
        newItem.img = currentItem.img;

        if (isNoName(newItem)) {
            Toast.makeText(this, "이름", Toast.LENGTH_SHORT).show();
            return;
        }
        RemoteService remoteService =
                ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertCustomerInfo(newItem);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String seq = response.body();
                    try {
                        currentItem.seq = Integer.parseInt(seq);
                        if (currentItem.seq == 0) {
                            Toast.makeText(context, R.string.member_insert_fail_message, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, R.string.member_insert_fail_message, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    currentItem.name = newItem.name;
                    currentItem.sextype = newItem.sextype;
                    currentItem.birthday = newItem.birthday;

                    ((MyApp) getApplicationContext()).setCustomer(currentItem);
                    Intent it = new Intent(ProfileActivity.this, customer_main.class);
                    startActivity(it);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });

    }

    /**
     * 프로필 아이콘이나 프로필 아이콘 변경 뷰를 클릭했을 때, 프로필 아이콘을 변경할 수 있도록
     * startProfileIconChange() 메소드를 호출한다.
     *
     * @param v 클릭한 뷰 객체
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.profile_icon || v.getId() == R.id.profile_icon_change) {
            startProfileIconChange();
        }
    }

    /**
     * ProfileIconActivity를 실행해서 프로필 아이콘을 변경할 수 있게 한다.
     */
    private void startProfileIconChange() {
        Intent intent = new Intent(this, ProfileIconActivity.class);
       // startActivity(intent);
        startActivityForResult(intent, 0);
    }
}