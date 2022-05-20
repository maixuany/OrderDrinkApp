package com.sinhvien.orderdrinkapp.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sinhvien.orderdrinkapp.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Base64;

public class AddMenuActivity extends AppCompatActivity{

    Button BTN_addmenu_ThemMon;
    RelativeLayout layout_trangthaimon;
    ImageView IMG_addmenu_ThemHinh, IMG_addmenu_back;
    TextView TXT_addmenu_title;
    TextInputLayout TXTL_addmenu_TenMon,TXTL_addmenu_GiaTien,TXTL_addmenu_LoaiMon;
    RadioGroup RG_addmenu_TinhTrang;
    RadioButton RD_addmenu_ConMon, RD_addmenu_HetMon;
    String tenloai, sTenMon,sGiaTien,sTinhTrang;
    Bitmap bitmapold;
    String maloai;
    String mamon = "";
    String tenmon = "";
    long giatien = 0L;
    int trangthai = 0;
    String ID="";

    ActivityResultLauncher<Intent> resultLauncherOpenIMG = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                        Uri uri = result.getData().getData();
                        try{
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            IMG_addmenu_ThemHinh.setImageBitmap(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmenu_layout);
        setControl();
        getData();
        setEvent();
    }

    private void getData() {
        maloai = getIntent().getStringExtra("maloai");
        tenloai = getIntent().getStringExtra("tenloai");
        ID = getIntent().getStringExtra("ID");
        mamon = getIntent().getStringExtra("mamon");
        tenmon = getIntent().getStringExtra("tenmon");
        giatien = getIntent().getLongExtra("giatien",0);
        trangthai = getIntent().getIntExtra("trangthai",0);
        System.out.println(maloai+" "+tenloai+" "+ID+" "+mamon+" "+tenmon+" "+giatien+" "+trangthai);
    }

    private void setEvent() {
        TXTL_addmenu_LoaiMon.getEditText().setText(tenloai);
        TXTL_addmenu_LoaiMon.getEditText().setFocusable(false);
        if(ID.equals("EDIT")){
            BTN_addmenu_ThemMon.setText("Sửa Món");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Mon");
            TXTL_addmenu_TenMon.getEditText().setText(tenmon);
            TXTL_addmenu_GiaTien.getEditText().setText(giatien+"");
            if(trangthai==0) RD_addmenu_HetMon.setChecked(true);
            else RD_addmenu_ConMon.setChecked(true);
        }else{

        }
        IMG_addmenu_ThemHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGetIMG = new Intent();
                iGetIMG.setType("image/*");
                iGetIMG.setAction(Intent.ACTION_GET_CONTENT);
                resultLauncherOpenIMG.launch(Intent.createChooser(iGetIMG,getResources().getString(R.string.choseimg)));
            }
        });

        IMG_addmenu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        BTN_addmenu_ThemMon.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                //ktra validation
                sTenMon = TXTL_addmenu_TenMon.getEditText().getText().toString();
                sGiaTien = TXTL_addmenu_GiaTien.getEditText().getText().toString();
                switch (RG_addmenu_TinhTrang.getCheckedRadioButtonId()){
                    case R.id.rd_addmenu_ConMon: sTinhTrang = "true";   break;
                    case R.id.rd_addmenu_HetMon: sTinhTrang = "false";  break;
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Mon");
                if(ID.equals("ADD")){
                    if(!validateImage() | !validateName() | !validatePrice()){
                        return;
                    }
                    myRef.child(mamon).child("MaLoaiMon").setValue(maloai);
                    myRef.child(mamon).child("TenMon").setValue(sTenMon);
                    if(sTinhTrang.equals("true"))
                        myRef.child(mamon).child("TinhTrang").setValue(1);
                    else
                        myRef.child(mamon).child("TinhTrang").setValue(0);
                    myRef.child(mamon).child("GiaTien").setValue(Long.parseLong(sGiaTien));
                    myRef.child(mamon).child("HinhAnh").setValue(bytesToString(imageViewtoByte(IMG_addmenu_ThemHinh)));
                }else{
                    if(!validateName() | !validatePrice()){
                        return;
                    }else{
                        myRef.child(mamon).child("MaLoaiMon").setValue(maloai);
                        myRef.child(mamon).child("TenMon").setValue(sTenMon);
                        if(sTinhTrang.equals("true"))
                            myRef.child(mamon).child("TinhTrang").setValue(1);
                        else
                            myRef.child(mamon).child("TinhTrang").setValue(0);
                        myRef.child(mamon).child("GiaTien").setValue(Long.parseLong(sGiaTien));
                        if(!validateImage()){

                        }else{
                            myRef.child(mamon).child("HinhAnh").setValue(bytesToString(imageViewtoByte(IMG_addmenu_ThemHinh)));
                        }
                    }
                }
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Object bytesToString(byte[] imageViewtoByte) {
        return Base64.getEncoder().encodeToString(imageViewtoByte);
    }

    private void setControl() {
        //region Lấy đối tượng view
        IMG_addmenu_ThemHinh = (ImageView)findViewById(R.id.img_addmenu_ThemHinh);
        IMG_addmenu_ThemHinh.setTag(R.drawable.drinkfood);
        IMG_addmenu_back = (ImageView)findViewById(R.id.img_addmenu_back);
        TXTL_addmenu_TenMon = (TextInputLayout)findViewById(R.id.txtl_addmenu_TenMon);
        TXTL_addmenu_GiaTien = (TextInputLayout)findViewById(R.id.txtl_addmenu_GiaTien);
        TXTL_addmenu_LoaiMon = (TextInputLayout)findViewById(R.id.txtl_addmenu_LoaiMon);
        BTN_addmenu_ThemMon = (Button)findViewById(R.id.btn_addmenu_ThemMon);
        TXT_addmenu_title = (TextView)findViewById(R.id.txt_addmenu_title);
        layout_trangthaimon = (RelativeLayout)findViewById(R.id.layout_trangthaimon);
        RG_addmenu_TinhTrang = (RadioGroup)findViewById(R.id.rg_addmenu_TinhTrang);
        RD_addmenu_ConMon = (RadioButton)findViewById(R.id.rd_addmenu_ConMon);
        RD_addmenu_HetMon = (RadioButton)findViewById(R.id.rd_addmenu_HetMon);
        BitmapDrawable olddrawable = (BitmapDrawable)IMG_addmenu_ThemHinh.getDrawable();
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

    //region Validate field
    private boolean validateImage(){
        BitmapDrawable drawable = (BitmapDrawable)IMG_addmenu_ThemHinh.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        if(bitmap == bitmapold){
            if(ID.equals("EDIT")){
                return false;
            }
            Toast.makeText(getApplicationContext(),"Xin chọn hình ảnh",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean validateName(){
        String val = TXTL_addmenu_TenMon.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            TXTL_addmenu_TenMon.setError(getResources().getString(R.string.not_empty));
            return false;
        }else {
            TXTL_addmenu_TenMon.setError(null);
            TXTL_addmenu_TenMon.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePrice(){
        String val = TXTL_addmenu_GiaTien.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            TXTL_addmenu_GiaTien.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(!val.matches(("\\d+(?:\\.\\d+)?"))){
            TXTL_addmenu_GiaTien.setError("Giá tiền không hợp lệ");
            return false;
        }else {
            TXTL_addmenu_GiaTien.setError(null);
            TXTL_addmenu_GiaTien.setErrorEnabled(false);
            return true;
        }
    }
    //endregion

}