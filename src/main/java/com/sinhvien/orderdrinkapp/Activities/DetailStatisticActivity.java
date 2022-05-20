package com.sinhvien.orderdrinkapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinhvien.orderdrinkapp.CustomAdapter.AdapterDisplayPayment;
import com.sinhvien.orderdrinkapp.Data.BanAn;
import com.sinhvien.orderdrinkapp.Data.NhanVien;
import com.sinhvien.orderdrinkapp.Data.ThanhToan;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;

public class DetailStatisticActivity extends AppCompatActivity  {

    ImageView img_detailstatistic_backbtn;
    TextView txt_detailstatistic_MaDon,txt_detailstatistic_NgayDat,txt_detailstatistic_TenBan
            ,txt_detailstatistic_TenNV,txt_detailstatistic_TongTien;
    ListView lvDetailStatistic;
    String madon, tenNV, tenban;
    String ngaydat;
    Long tongtien;
    ArrayList<ThanhToan> data = new ArrayList<>();
    AdapterDisplayPayment adapterDisplayPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailstatistic_layout);
        setControl();
        getData();
        setEvent();
    }

    private void setEvent() {
        img_detailstatistic_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    private void getData() {
        //Lấy thông tin từ display statistic
        Intent intent = getIntent();
        madon = intent.getStringExtra("madon");
        tenNV = intent.getStringExtra("tenNV");
        tenban = intent.getStringExtra("tenban");
        ngaydat = intent.getStringExtra("ngaydat");
        tongtien = intent.getLongExtra("tongtien",0);
        txt_detailstatistic_TongTien.setText(tongtien+" VND");
        txt_detailstatistic_NgayDat.setText(ngaydat);
        txt_detailstatistic_MaDon.setText(madon);
        txt_detailstatistic_TenNV.setText(tenNV);
        txt_detailstatistic_TenBan.setText(tenban);
        HienThiDSCTDD();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 = database.getReference("ThanhToan");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for(DataSnapshot item: snapshot.getChildren()){
                    ThanhToan thanhToan = item.getValue(ThanhToan.class);
                    if(thanhToan.getMaDonDat().equals(madon)) data.add(thanhToan);
                }
                adapterDisplayPayment.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        //region Thuộc tính bên view
        img_detailstatistic_backbtn = (ImageView)findViewById(R.id.img_detailstatistic_backbtn);
        txt_detailstatistic_MaDon = (TextView)findViewById(R.id.txt_detailstatistic_MaDon);
        txt_detailstatistic_NgayDat = (TextView)findViewById(R.id.txt_detailstatistic_NgayDat);
        txt_detailstatistic_TenBan = (TextView)findViewById(R.id.txt_detailstatistic_TenBan);
        txt_detailstatistic_TenNV = (TextView)findViewById(R.id.txt_detailstatistic_TenNV);
        txt_detailstatistic_TongTien = (TextView)findViewById(R.id.txt_detailstatistic_TongTien);
        lvDetailStatistic = findViewById(R.id.lvDetailStatistic);
        //endregion
    }

    private void HienThiDSCTDD(){
        adapterDisplayPayment = new AdapterDisplayPayment(this,R.layout.custom_layout_paymentmenu,data);
        lvDetailStatistic.setAdapter(adapterDisplayPayment);
        adapterDisplayPayment.notifyDataSetChanged();
    }



}