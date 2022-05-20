package com.sinhvien.orderdrinkapp.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinhvien.orderdrinkapp.Data.LoaiMon;
import com.sinhvien.orderdrinkapp.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import java.util.Base64;

public class AddCategoryActivity extends AppCompatActivity{

    Button BTN_addcategory_TaoLoai;
    ImageView IMG_addcategory_back, IMG_addcategory_ThemHinh;
    TextView TXT_addcategory_title;
    TextInputLayout TXTL_addcategory_TenLoai;
    ArrayList<LoaiMon> listLM = new ArrayList<>();
    Bitmap bitmapold;
    LoaiMon loaiMon = new LoaiMon();
    String ID="";
    String role = "";

    ActivityResultLauncher<Intent> resultLauncherOpenIMG = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                        Uri uri = result.getData().getData();
                        try{
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            IMG_addcategory_ThemHinh.setImageBitmap(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcategory_layout);
        getData();
        setControl();
        setEvent();
        ID = getIntent().getStringExtra("ID");
        if(ID.equals("EDIT")) {
            BTN_addcategory_TaoLoai.setText("Sửa menu");
            String maLoaiMon = getIntent().getStringExtra("maLoaiMon");
            String tenLoai = getIntent().getStringExtra("tenLoai");
            loaiMon.setMaLoaiMon(maLoaiMon);
            loaiMon.setTenLoai(tenLoai);
            TXTL_addcategory_TenLoai.getEditText().setText(loaiMon.getTenLoai());
        }
    }

    private void setEvent() {
        IMG_addcategory_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        IMG_addcategory_ThemHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGetIMG = new Intent();
                iGetIMG.setType("image/*"); //lấy những mục chứa hình ảnh
                iGetIMG.setAction(Intent.ACTION_GET_CONTENT);   //lấy mục hiện tại đang chứa hình
                resultLauncherOpenIMG.launch(Intent.createChooser(iGetIMG,getResources().getString(R.string.choseimg)));
            }
        });

        BTN_addcategory_TaoLoai.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("LoaiMon");
                if(ID.equals("EDIT")){
                    if(!validateName()){
                        return;
                    }else{
                        if(!validateImage()){
                            myRef.child(loaiMon.getMaLoaiMon()).child("TenLoai").setValue(TXTL_addcategory_TenLoai.getEditText().getText().toString());
                        }else{
                            myRef.child(loaiMon.getMaLoaiMon()).child("TenLoai").setValue(TXTL_addcategory_TenLoai.getEditText().getText().toString());
                            myRef.child(loaiMon.getMaLoaiMon()).child("HinhAnh").setValue(bytesToString(imageViewtoByte(IMG_addcategory_ThemHinh)));
                        }
                    }
                }else{
                    if(!validateImage() | !validateName()){
                        return;
                    }else{
                        String maloai = String.valueOf(UUID.randomUUID());
                        myRef.child(maloai).child("TenLoai").setValue(TXTL_addcategory_TenLoai.getEditText().getText().toString());
                        myRef.child(maloai).child("HinhAnh").setValue(bytesToString(imageViewtoByte(IMG_addcategory_ThemHinh)));
                    }
                }
                finish();
            }
        });

    }

    private void getData() {
        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        role = sharedPreferences.getString("role","");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("LoaiMon");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listLM.clear();
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    LoaiMon a = item.getValue(LoaiMon.class);
                    a.setMaLoaiMon(item.getKey());
                    listLM.add(a);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setControl() {
        BTN_addcategory_TaoLoai = (Button)findViewById(R.id.btn_addcategory_TaoLoai);
        TXTL_addcategory_TenLoai = (TextInputLayout)findViewById(R.id.txtl_addcategory_TenLoai);
        IMG_addcategory_back = (ImageView)findViewById(R.id.img_addcategory_back);
        IMG_addcategory_ThemHinh = (ImageView)findViewById(R.id.img_addcategory_ThemHinh);
        TXT_addcategory_title = (TextView)findViewById(R.id.txt_addcategory_title);
        BitmapDrawable olddrawable = (BitmapDrawable)IMG_addcategory_ThemHinh.getDrawable();
        bitmapold = olddrawable.getBitmap();
    }

    //Chuyển ảnh bitmap về mảng byte lưu vào csdl
    private byte[] imageViewtoByte(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String bytesToString(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }

    //region validate fields
    private boolean validateImage(){
        BitmapDrawable drawable = (BitmapDrawable)IMG_addcategory_ThemHinh.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        if(bitmap == bitmapold){
            if(ID.equals("EDIT")){
                return false;
            }Toast.makeText(getApplicationContext(),"Xin chọn hình ảnh",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean validateName(){
        String val = TXTL_addcategory_TenLoai.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            TXTL_addcategory_TenLoai.setError(getResources().getString(R.string.not_empty));
            return false;
        }else {
            TXTL_addcategory_TenLoai.setError(null);
            TXTL_addcategory_TenLoai.setErrorEnabled(false);
            return true;
        }
    }
    //endregion

}