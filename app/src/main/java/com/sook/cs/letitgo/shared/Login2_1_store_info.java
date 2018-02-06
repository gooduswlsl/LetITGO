package com.sook.cs.letitgo.shared;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sook.cs.letitgo.MyApp;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.lib.EtcLib;
import com.sook.cs.letitgo.lib.RemoteLib;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sook.cs.letitgo.R.id.addressView;

public class Login2_1_store_info extends AppCompatActivity {
    Context context;
    Seller currentItem;
    File croppedFileName;
    String imageFileName;

    private ImageView imgMain;
    private Button addressbtn;
    private TextView addressText;
    private EditText nameEdit;
    private EditText telEdit;
    private EditText siteEdit;
    private EditText webpageEdit;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_ALBUM = 2;
    private static final int CROP_FROM_CAMERA = 3;
    private static final int GET_ADDRESS = 4;

    private Uri photoUri;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private static final int MULTIPLE_PERMISSIONS = 101;

    private String mCurrentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentItem = ((MyApp) getApplication()).getSeller();
        context = this;


        setContentView(R.layout.activity_main3);
        checkPermissions();
        initView();
    }

    private void initView(){
        imgMain = (ImageView) findViewById(R.id.img_test);
        addressbtn = (Button) findViewById(R.id.button4);
        addressText = (TextView) findViewById(addressView);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        telEdit = (EditText) findViewById(R.id.telEdit);
        siteEdit = (EditText) findViewById(R.id.siteEdit);
        webpageEdit = (EditText) findViewById(R.id.webpageEdit);
    }

    private void save() {

        uploadProfileIcon();
        final Seller newItem = getSellerInfoItem();

        RemoteService remoteService =
                ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertSellerInfo(newItem);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String seq = response.body();
                    try {
                        currentItem.seq = Integer.parseInt(seq);
                        if (currentItem.seq == 0) {
                            Toast.makeText(context, R.string.member_insert_fail_message, Toast.LENGTH_SHORT);
                            return;
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, R.string.member_insert_fail_message, Toast.LENGTH_SHORT);
                        return;
                    }
                    currentItem.name = newItem.name;
                    currentItem.site = newItem.site;
                    currentItem.tel = newItem.tel;
                    currentItem.address = newItem.address;
                    currentItem.webpage = newItem.webpage;

                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public void nextbtn(View v){
        save();
        Intent intent = new Intent(getApplicationContext(),Login_finish.class);
        startActivity(intent);
    }

//    public void getGeo(View v){
//        final Geocoder geocoder = new Geocoder(this);
//        List<Address> list = null;
//
//        String address = addressText.getText().toString();
//        Log.d("test",address);
//
//        try{
//            list = geocoder.getFromLocationName(address,5);
//        }catch(IOException e){
//            e.printStackTrace();
//            Log.d("test","주소를 좌표로 변환시 에러");
//        }
//
//        if (list != null){
//            if (list.size() == 0){
//                Toast.makeText(getApplicationContext(),"해당되는 주소가 없습니다.",Toast.LENGTH_SHORT).show();
//            }else {
//                double latitude = list.get(0).getLatitude();
//                double longitude = list.get(0).getLongitude();
//                Toast.makeText(getApplicationContext(), latitude + "/" + longitude, Toast.LENGTH_LONG).show();
//            }
//        }
//
//    }



    private Seller getSellerInfoItem(){
        Seller item = new Seller();
        // item.phone = EtcLib.getInstance().getPhoneNumber(context);
        item.phone= EtcLib.getInstance().getPhoneNumber(context);
        item.name = nameEdit.getText().toString();
        item.site = siteEdit.getText().toString();
        item.tel = telEdit.getText().toString();
        item.address = addressText.getText().toString();
        item.webpage = webpageEdit.getText().toString();
        item.img = currentItem.img;

        return item;
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
            Toast.makeText(Login2_1_store_info.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(Login2_1_store_info.this,
                    "com.sook.cs.letitgo.provider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        imageFileName = "IP" + timeStamp + "_"; //
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/NOSTest/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
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
            if (data == null) {
                return;
            }
            photoUri = data.getData();
            cropImage();
        } else if (requestCode == PICK_FROM_CAMERA) {
            cropImage();
            // 갤러리에 나타나게
            MediaScannerConnection.scanFile(Login2_1_store_info.this,
                    new String[]{photoUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } else if (requestCode == CROP_FROM_CAMERA) {
            imgMain.setImageURI(null);
            imgMain.setImageURI(photoUri);

        }else if (requestCode == GET_ADDRESS){ //주소 찾아오기 버튼 눌러서 주소 받아온 후
            String address = data.getStringExtra("address");
            addressText.setText(Html.fromHtml("<u>" + address + "</u>"));
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

            photoUri = FileProvider.getUriForFile(Login2_1_store_info.this,
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
        currentItem.img =  imageFileName;
        Log.d("ddtest",""+imageFileName);

    }



}