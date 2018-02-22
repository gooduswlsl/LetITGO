package com.sook.cs.letitgo.seller;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sook.cs.letitgo.MyApp;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.customer.customer_leave;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.lib.RemoteLib;
import com.sook.cs.letitgo.lib.StringLib;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;
import com.sook.cs.letitgo.shared.Login_2_1_1_searchAddress;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class seller_edit_profile extends AppCompatActivity {
    Context context;
    Seller current_seller;
    File croppedFileName;
    String location;
    String imageName;

    private ImageView imgMain;
    private TextView addressText, leaveMember;
    private EditText nameEdit, telEdit, siteEdit, webpageEdit;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_ALBUM = 2;
    private static final int CROP_FROM_CAMERA = 3;
    private static final int GET_ADDRESS = 4;

    private boolean address_btn_clicked = false;
    private boolean img_btn_clicked = false;

    private Uri photoUri;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private static final int MULTIPLE_PERMISSIONS = 101;

    private String mCurrentPhotoPath;
    private final String TAG = this.getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTitle("회원 정보 수정");

        current_seller = ((MyApp) getApplication()).getSeller();
        context = this;

        setContentView(R.layout.seller_edit_profile);
        checkPermissions();
        initView();
    }

    private void initView(){
        imgMain = (ImageView) findViewById(R.id.img_test);
        addressText = (TextView) findViewById(R.id.addressView);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        telEdit = (EditText) findViewById(R.id.telEdit);
        siteEdit = (EditText) findViewById(R.id.siteEdit);
        webpageEdit = (EditText) findViewById(R.id.webpageEdit);
        leaveMember = (TextView) findViewById(R.id.leaveMember);

        showPicture();
        addressText.setText(current_seller.address);
        nameEdit.setText(current_seller.name);
        telEdit.setText(current_seller.tel);
        siteEdit.setText(current_seller.site);
        webpageEdit.setText(current_seller.webpage);

        leaveMember.setOnClickListener(new View.OnClickListener() {  //회원탈퇴
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(seller_edit_profile.this);
                alert_confirm.setMessage("정말 탈퇴하시겠습니까?").setCancelable(false).setPositiveButton("확인",

                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { //확인
                                leaveSeller();
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

    public void save(View v) {

        if(img_btn_clicked)
            uploadProfileIcon();
        if(address_btn_clicked)
            getGeo();

        current_seller.name = nameEdit.getText().toString();
        current_seller.site = siteEdit.getText().toString();
        current_seller.tel = telEdit.getText().toString();
        current_seller.webpage = webpageEdit.getText().toString();

        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Log.d(TAG,current_seller.toString());
        Call<String> call = remoteService.changeSellerInfo(current_seller);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG,"seller회원정보 수정 성공");
                    Toast.makeText(getApplicationContext(),"회원정보 수정 완료!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Seller_main.class);
                    startActivity(intent);
                }
                else
                    Log.d(TAG,"seller 회원정보 수정 실패");

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public void getGeo(){
        final Geocoder geocoder = new Geocoder(this);
        List<Address> list = null;


        Log.d(TAG,"location:"+location);
        String address = location;

        try{
            list = geocoder.getFromLocationName(address,5);
        }catch(IOException e){
            e.printStackTrace();
            Log.d("test","주소를 좌표로 변환시 에러");
        }

        if (list != null){
            if (list.size() == 0){
                Toast.makeText(getApplicationContext(),"해당되는 주소가 없습니다.",Toast.LENGTH_SHORT).show();
            }else {
                current_seller.latitude = list.get(0).getLatitude();
                current_seller.longitude = list.get(0).getLongitude();
            }
        }

    }

    public void selectPicture(View v){
        final String items[] = {"카메라로 촬영","갤러리에서 가져오기"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("선택하세요")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){ //카메라로 촬영
                            takePhoto();
                        }else if(which == 1){ //갤러리에서 가져오기
                            goToAlbum();
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(seller_edit_profile.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(seller_edit_profile.this,
                    "com.sook.cs.letitgo.provider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "IP" + timeStamp + "_"; //
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/NOSTest/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageName = image.getName(); //저장되는 이미지파일의 이름
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }

    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void getAddress(View v){
        Intent intent = new Intent(getApplicationContext(),Login_2_1_1_searchAddress.class);
        startActivityForResult(intent,GET_ADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == PICK_FROM_ALBUM) {
            img_btn_clicked=true;
            if (data == null) {
                return;
            }
            photoUri = data.getData();
            cropImage();
        } else if (requestCode == PICK_FROM_CAMERA) {
            img_btn_clicked=true;
            cropImage();
            // 갤러리에 나타나게
            MediaScannerConnection.scanFile(seller_edit_profile.this,
                    new String[]{photoUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } else if (requestCode == CROP_FROM_CAMERA) {
            img_btn_clicked=true;
            imgMain.setImageURI(null);
            imgMain.setImageURI(photoUri);

        }else if (requestCode == GET_ADDRESS){ //주소 찾아오기 버튼 눌러서 주소 받아온 후
            address_btn_clicked=true;
            current_seller.address = data.getStringExtra("address");
            location = data.getStringExtra("location");
            addressText.setText(Html.fromHtml("<u>" + current_seller.address + "</u>"));
        }
    }

    public void cropImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.grantUriPermission("com.android.camera", photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            grantUriPermission(list.get(0).activityInfo.packageName, photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            croppedFileName = null; ///
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + "/NOSTest/");
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            photoUri = FileProvider.getUriForFile(seller_edit_profile.this,
                    "com.sook.cs.letitgo.provider", tempFile);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                grantUriPermission(res.activityInfo.packageName, photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            startActivityForResult(i, CROP_FROM_CAMERA);
        }
    }

    /**
     * 프로필 아이콘을 서버에 업로드한다.
     */
    private void uploadProfileIcon() {
        RemoteLib.getInstance().uploadSellerImg(croppedFileName);
        current_seller.img =  imageName;

    }

    private void showPicture(){
        if (StringLib.getInstance().isBlank(current_seller.img)) {
            Picasso.with(this).load(R.drawable.noimage).into(imgMain);
        } else {
            Picasso.with(this)
                    .load(RemoteService.SELLER_IMG_URL + current_seller.img)
                    .into(imgMain);
        }
    }

    // 회원 탈퇴
    private void leaveSeller() {
        Log.d(TAG, current_seller.toString());
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.leaveSeller(current_seller.getSeq());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), customer_leave.class);
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


}