package com.sinhvien.orderdrinkapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinhvien.orderdrinkapp.Data.NhanVien;
import com.sinhvien.orderdrinkapp.R;

import java.util.regex.Pattern;

public class ChangePassword extends AppCompatActivity {
    TextInputLayout txtl_changepass_Code, txtl_changepass_MatKhau;
    Button btn_changepass_submit;
    String manv;
    NhanVien nhanVien = null;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{6,}" +                // at least 4 characters
                    "$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setControl();
        getData();
        setEvent();
    }

    private void setControl(){
        btn_changepass_submit = findViewById(R.id.btn_changepass_submit);
        txtl_changepass_Code = findViewById(R.id.txtl_changepass_Code);
        txtl_changepass_MatKhau = findViewById(R.id.txtl_changepass_MatKhau);
    }

    private void getData(){
        Intent intent = getIntent();
        manv = intent.getStringExtra("manv");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("NhanVien");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item: snapshot.getChildren()){
                    NhanVien a = item.getValue(NhanVien.class);
                    a.setMaNV(item.getKey());
                    if(a.getMaNV()!=null){
                        if(a.getMaNV().equals(manv)) nhanVien = a;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setEvent(){
        btn_changepass_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validatePassWord() | !validateCode()){
                    return;
                }else{
                    String mk = txtl_changepass_MatKhau.getEditText().getText().toString().trim();
                    String code = txtl_changepass_Code.getEditText().getText().toString().trim();
                    if(nhanVien.getRecoveryCode().equals(code)){
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("NhanVien");
                        myRef.child(manv).child("MatKhau").setValue(mk);
                        myRef.child(manv).child("RecoveryCode").setValue("null");
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Code không chính xác", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateCode() {
        String val = txtl_changepass_Code.getEditText().getText().toString().trim();


        if(val.isEmpty()){
            txtl_changepass_Code.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(val.length() != 6){
            txtl_changepass_Code.setError("Code không hợp lệ!");
            return false;
        }
        else {
            txtl_changepass_Code.setError(null);
            txtl_changepass_Code.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassWord() {
        String val = txtl_changepass_MatKhau.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            txtl_changepass_MatKhau.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(!PASSWORD_PATTERN.matcher(val).matches()){
            txtl_changepass_MatKhau.setError("Mật khẩu ít nhất 6 ký tự!");
            return false;
        }
        else {
            txtl_changepass_MatKhau.setError(null);
            txtl_changepass_MatKhau.setErrorEnabled(false);
            return true;
        }
    }
}