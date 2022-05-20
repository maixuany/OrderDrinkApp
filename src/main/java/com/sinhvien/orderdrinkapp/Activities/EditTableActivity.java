package com.sinhvien.orderdrinkapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sinhvien.orderdrinkapp.R;

public class EditTableActivity extends AppCompatActivity {

    TextInputLayout TXTL_edittable_tenban;
    Button BTN_edittable_SuaBan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edittable_layout);
        //thuộc tính view
        setControl();
        setEvent();
    }

    private void setEvent() {
        String maban = getIntent().getStringExtra("maban"); //lấy maban từ bàn đc chọn
        // nút lưu sửa
        BTN_edittable_SuaBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenban = TXTL_edittable_tenban.getEditText().getText().toString();

                if(tenban != null || tenban.equals("")){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("BanAn");
                    ref.child(maban).child("TenBanAn").setValue(tenban);
                    finish();
                }
            }
        });
    }

    private void setControl() {
        TXTL_edittable_tenban = (TextInputLayout)findViewById(R.id.txtl_edittable_tenban);
        BTN_edittable_SuaBan = (Button)findViewById(R.id.btn_edittable_SuaBan);
    }
}