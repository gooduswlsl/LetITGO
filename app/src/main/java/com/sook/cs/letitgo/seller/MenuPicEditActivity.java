package com.sook.cs.letitgo.seller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sook.cs.letitgo.MyApp;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.lib.FileLib;
import com.sook.cs.letitgo.lib.RemoteLib;
import com.sook.cs.letitgo.lib.StringLib;
import com.sook.cs.letitgo.remote.RemoteService;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * 프로필 아이콘을 등록하는 액티비티
 */
public class MenuPicEditActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int CROP_FROM_ALBUM = 3;

    Context context;
    ImageView MenuIconImage;
    Menu MenuItem;
    File MenuFile;
    String menuFilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_menupic);
        Intent intent=getIntent();

        context = this;

        MenuItem = ((MyApp) getApplication()).getMenu();
        MenuItem=(Menu)intent.getSerializableExtra("item");

        Log.d(TAG,"mSeq는 "+MenuItem.getmSeq());
        setActionbar();
        setView();
        setProfileIcon();
    }
    /**
     * 액티비티 툴바를 설정한다.
     */
    private void setActionbar() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("메뉴 사진 설정");
        }
    }

    /**
     * 액티비티 화면을 설정한다.
     */
    public void setView() {
        MenuIconImage = (ImageView) findViewById(R.id.profile_icon);

        Button albumButton = (Button) findViewById(R.id.album);
        albumButton.setOnClickListener(this);

        Button cameraButton = (Button) findViewById(R.id.camera);
        cameraButton.setOnClickListener(this);
    }

    /**
     * 프로필 아이콘을 설정한다.
     */
    private void setProfileIcon() {

        if (StringLib.getInstance().isBlank( MenuItem.mImgUrl)) { //사진 없을때 기본사진으로
            Picasso.with(this).load(R.drawable.noimage).into(MenuIconImage);
        } else { //사진 있을 때
            Picasso.with(this)
                    .load(RemoteService.MENU_IMG_URL +  MenuItem.mImgUrl)
                    .into(MenuIconImage);
        }
    }

    /**
     * 사용자가 선택한 프로필 아이콘을 저장할 파일 이름을 설정한다.
     */
    private void setProfileIconFile() {
        menuFilename =  MenuItem.getmSeq() + "_" + String.valueOf(System.currentTimeMillis());
//        MenuItem.mImgUrl=menuFilename; //내가 그냥 추가해줌.
        MenuFile = FileLib.getInstance().getProfileIconFile(context, menuFilename);
    }

    /**
     * 프로필 아이콘을 설정하기 위해 선택할 수 있는 앨범이나 카메라 버튼의 클릭 이벤트를 처리한다.
     *
     * @param v 클릭한 뷰 객체
     */
    @Override
    public void onClick(View v) {
        setProfileIconFile();

        if (v.getId() == R.id.album) {
            getImageFromAlbum();

        } else if (v.getId() == R.id.camera) {
            getImageFromCamera();
        }
    }

    /**
     * 오른쪽 상단 메뉴를 구성한다.
     * 닫기 메뉴만이 설정되어 있는 menu_close.xml를 지정한다.
     *
     * @param menu 메뉴 객체
     * @return 메뉴를 보여준다면 true, 보여주지 않는다면 false
     */
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close, menu);
        return true;
    }

    /**
     * 왼쪽 화살표 메뉴(android.R.id.home)를 클릭했을 때와
     * 오른쪽 상단 닫기 메뉴를 클릭했을 때의 동작을 지정한다.
     * 여기서는 모든 버튼이 액티비티를 종료한다.
     *
     * @param item 메뉴 아이템 객체
     * @return 메뉴를 처리했다면 true, 그렇지 않다면 false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_close:
                Intent intent=new Intent();
                intent.putExtra("filename",menuFilename);
                this.setResult(RESULT_OK,intent);
                finish();
                break;
        }
        return true;
    }

    /**
     * 카메라 앱을 실행해서 이미지를 촬영한다.
     */
    private void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(MenuFile));
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    /**
     * 카메라 앨범앱을 실행해서 이미지를 선택한다.
     */
    private void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    /**
     * 이미지를 자르기 위한 Intent를 생성해서 반환한다.
     *
     * @param inputUri  이미지를 자르기전 Uri
     * @param outputUri 이미지를 자른 결과 파일 Uri
     * @return 이미지를 자르기 위한 인텐트
     */
    private Intent getCropIntent(Uri inputUri, Uri outputUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inputUri, "image/*");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());

        return intent;
    }

    /**
     * 카메라에서 촬영한 이미지를 프로필 아이콘에 사용할 크기로 자른다.
     */
    private void cropImageFromCamera() {
        Uri uri = Uri.fromFile(MenuFile);
        Intent intent = getCropIntent(uri, uri);
        startActivityForResult(intent, CROP_FROM_CAMERA);
    }

    /**
     * 카메라 앨범에서 선택한 이미지를 프로필 아이콘에 사용할 크기로 자른다.
     */
    private void cropImageFromAlbum(Uri inputUri) {
        Uri outputUri = Uri.fromFile(MenuFile);

        Log.d(TAG, "startPickFromAlbum uri " + inputUri.toString());
        Intent intent = getCropIntent(inputUri, outputUri);
        startActivityForResult(intent, CROP_FROM_ALBUM);
    }

    /**
     * startActivityForResult() 메소드로 호출한 액티비티의 결과를 처리한다.
     *
     * @param requestCode 액티비티를 실행하면서 전달한 요청 코드
     * @param resultCode  실행한 액티비티가 설정한 결과 코드
     * @param intent      결과 데이터
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG, "onActivityResult " + intent);

        if (resultCode != RESULT_OK) return;

        if (requestCode == PICK_FROM_CAMERA) {
            Picasso.with(this).load(MenuFile).into(MenuIconImage);
            Log.d(TAG,"cropImageFromCamera");
            cropImageFromCamera();

        } else if (requestCode == CROP_FROM_CAMERA) {
            Picasso.with(this).load(MenuFile).into(MenuIconImage);
            uploadProfileIcon();

        } else if (requestCode == PICK_FROM_ALBUM && intent != null) {
            Uri dataUri = intent.getData();
            if (dataUri != null) {
                cropImageFromAlbum(dataUri);
            }
        } else if (requestCode == CROP_FROM_ALBUM && intent != null) {
            Picasso.with(this).load(MenuFile).into(MenuIconImage);
            uploadProfileIcon();
        }
    }

    /**
     * 프로필 아이콘을 서버에 업로드한다.
     */
    private void uploadProfileIcon() {
        RemoteLib.getInstance().uploadPicIcon(MenuFile);
    }
}
