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
import com.sinhvien.orderdrinkapp.Services.SendMailService;

import java.util.ArrayList;
import java.util.Random;

public class SendMail extends AppCompatActivity {
    TextInputLayout txtl_sendmail_Email;
    Button btn_sendmail_Send;
    ArrayList<NhanVien> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);
        setControl();
        getData();
        setEvent();
    }

    private void setEvent() {
        btn_sendmail_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateEmail()) {
                    return;
                }else {
                    String email = txtl_sendmail_Email.getEditText().getText().toString().trim();
                    NhanVien nhanVien=null;
                    for (NhanVien i : list) {
                        if (i.getEmail().equals(email)) nhanVien = i;
                    }
                    if(nhanVien!=null) {
                        Random rnd = new Random();
                        int number = rnd.nextInt(999999);
                        String subject = "Forgot Password - Code Recovery";
                        String message = String.format("%06d", number);
                        SendMailService sendMailService = new SendMailService(SendMail.this, txtl_sendmail_Email.getEditText().getText().toString().trim(), subject, message);
                        sendMailService.execute();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("NhanVien");
                        myRef.child(nhanVien.getMaNV()).child("RecoveryCode").setValue(message);
                        finish();
                        Intent intent = new Intent(SendMail.this, ChangePassword.class);
                        intent.putExtra("manv",nhanVien.getMaNV());
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"Email chưa được đăng kí",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void getData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("NhanVien");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    NhanVien a = item.getValue(NhanVien.class);
                    a.setMaNV(item.getKey());
                    list.add(a);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        txtl_sendmail_Email = findViewById(R.id.txtl_sendmail_Email);
        btn_sendmail_Send = findViewById(R.id.btn_sendmail_Send);
    }

    private boolean validateEmail() {
        String val = txtl_sendmail_Email.getEditText().getText().toString().trim();
        String checkspaces = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            txtl_sendmail_Email.setError(getResources().getString(R.string.not_empty));
            return false;
        } else if (!val.matches(checkspaces)) {
            txtl_sendmail_Email.setError("Email không hợp lệ!");
            return false;
        } else {
            txtl_sendmail_Email.setError(null);
            txtl_sendmail_Email.setErrorEnabled(false);
            return true;
        }
    }
}