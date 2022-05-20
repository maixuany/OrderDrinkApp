package com.sinhvien.orderdrinkapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinhvien.orderdrinkapp.Data.NhanVien;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;

public class
LoginActivity extends AppCompatActivity {
    Button BTN_login_DangNhap, BTN_login_QuenMatKhau;
    TextInputLayout TXTL_login_TenDN, TXTL_login_MatKhau;
    CheckBox checkboxLuu;
    ArrayList<NhanVien> listNV = new ArrayList<>();
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        String remember = sharedPreferences.getString("remember","");
        if(remember.equals("true")){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
        //thuộc tính view
        setControl();
        getData();
        setEvent();


    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void setEvent() {
        BTN_login_DangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateUserName() | !validatePassWord()){
                    return;
                }
                String tenDN = TXTL_login_TenDN.getEditText().getText().toString();
                String matKhau = TXTL_login_MatKhau.getEditText().getText().toString();
                for(NhanVien nv: listNV){
                    if(tenDN.equals(nv.getTenDN())&&matKhau.equals(nv.getMatKhau())){
                        System.out.println(nv.toString());
                        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
                        SharedPreferences.Editor editor =sharedPreferences.edit();
                        editor.putString("role", nv.getMaQuyen());
                        editor.putString("username", nv.getTenDN());
                        editor.putString("maNV", nv.getMaNV());
                        editor.putString("tenNV", nv.getHoVaTen());
                        if(checkboxLuu.isChecked()) {
                            editor.putString("remember", "true");
                            System.out.println("Lưu Mật Khẩu");
                        }
                        else{
                            editor.putString("remember","false");
                            System.out.println("Không lưu mật khẩu");
                        }
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                }
                Toast.makeText(getApplicationContext(),"Đăng nhập thất bại!",Toast.LENGTH_SHORT).show();
            }
        });

        BTN_login_QuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SendMail.class);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("NhanVien");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listNV.clear();
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    NhanVien nv = item.getValue(NhanVien.class);
                    nv.setMaNV(item.getKey());
                    listNV.add(nv);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setControl() {
        TXTL_login_TenDN = (TextInputLayout)findViewById(R.id.txtl_login_TenDN);
        TXTL_login_MatKhau = (TextInputLayout)findViewById(R.id.txtl_login_MatKhau);
        BTN_login_DangNhap = (Button)findViewById(R.id.btn_login_DangNhap);
        BTN_login_QuenMatKhau = (Button)findViewById(R.id.btn_login_QuenMatKhau);
        checkboxLuu = findViewById(R.id.checkboxLuu);
    }

    //region Validate field
    private boolean validateUserName(){
        String val = TXTL_login_TenDN.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            TXTL_login_TenDN.setError(getResources().getString(R.string.not_empty));
            return false;
        }else {
            TXTL_login_TenDN.setError(null);
            TXTL_login_TenDN.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassWord(){
        String val = TXTL_login_MatKhau.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            TXTL_login_MatKhau.setError(getResources().getString(R.string.not_empty));
            return false;
        }else{
            TXTL_login_MatKhau.setError(null);
            TXTL_login_MatKhau.setErrorEnabled(false);
            return true;
        }
    }
    //endregion
}