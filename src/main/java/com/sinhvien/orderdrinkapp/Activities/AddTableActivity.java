package com.sinhvien.orderdrinkapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sinhvien.orderdrinkapp.R;

public class AddTableActivity extends AppCompatActivity {

    TextInputLayout TXTL_addtable_tenban;
    Button BTN_addtable_TaoBan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtable_layout);

        //region Lấy đối tượng trong view
        TXTL_addtable_tenban = (TextInputLayout)findViewById(R.id.txtl_addtable_tenban);
        BTN_addtable_TaoBan = (Button)findViewById(R.id.btn_addtable_TaoBan);

        //khởi tạo dao mở kết nối csdl
        String maban = getIntent().getStringExtra("maban"); //lấy maban từ bàn đc chọn

        BTN_addtable_TaoBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sTenBanAn = TXTL_addtable_tenban.getEditText().getText().toString();
                if(sTenBanAn != null || sTenBanAn.equals("")){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("BanAn");
                    myRef.child(maban).child("TenBanAn").setValue(sTenBanAn);
                    myRef.child(maban).child("TrangThai").setValue(0);
                    myRef.child(maban).child("MaDonDatHienTai").setValue("null");
                    finish();
                }
            }
        });
    }

    //validate dữ liệu
    private boolean validateName(){
        String val = TXTL_addtable_tenban.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            TXTL_addtable_tenban.setError(getResources().getString(R.string.not_empty));
            return false;
        }else {
            TXTL_addtable_tenban.setError(null);
            TXTL_addtable_tenban.setErrorEnabled(false);
            return true;
        }
    }
}