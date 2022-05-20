package com.sinhvien.orderdrinkapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.sinhvien.orderdrinkapp.Data.ChiTietDonDat;
import org.apache.commons.lang3.RandomStringUtils;
import com.sinhvien.orderdrinkapp.Data.DonDat;
import com.sinhvien.orderdrinkapp.Data.Mon;
import com.sinhvien.orderdrinkapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AmountMenuActivity extends AppCompatActivity {

    TextInputLayout TXTL_amountmenu_SoLuong;
    Button BTN_amountmenu_DongY;
    String maban, mamon, maNV, tenmon, tenban, hoten, hinhanh;
    Long giatien = 0L;
    Long tongtien = 0L;
    ArrayList<DonDat> listDonDat = new ArrayList<>();
    String role = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amount_menu_layout);
        setControl();
        getData();
        setEvent();
    }

    private void getData(){
        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        role = sharedPreferences.getString("role","");
        tenban = sharedPreferences.getString("tenban","");
        hoten = sharedPreferences.getString("tenNV","");
        Intent intent = getIntent();
        tenmon = intent.getStringExtra("tenmon");
        mamon = intent.getStringExtra("mamon");
        giatien = intent.getLongExtra("giatien",0);
        getDataDonDat();
        getDataHinhAnh();
    }


    private void getDataDonDat() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DonDat");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listDonDat.clear();
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    DonDat a = item.getValue(DonDat.class);
                    a.setMaDonDat(item.getKey());
                    listDonDat.add(a);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDataHinhAnh() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Mon");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Mon a = item.getValue(Mon.class);
                    a.setMaMon(item.getKey());
                    if(a.getMaMon()!=null){
                        if(a.getMaMon().equals(mamon)) hinhanh = a.getHinhAnh();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setControl() {
        //Lấy đối tượng view
        TXTL_amountmenu_SoLuong = (TextInputLayout)findViewById(R.id.txtl_amountmenu_SoLuong);
        BTN_amountmenu_DongY = (Button)findViewById(R.id.btn_amountmenu_DongY);
    }

    private void setEvent() {
        BTN_amountmenu_DongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateAmount()) {
                    return;
                }
                SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
                maban = sharedPreferences.getString("maban","");
                String taobanmoi = sharedPreferences.getString("taobanmoi","");
                maNV = sharedPreferences.getString("maNV","");
                System.out.println("MAI XUAN Y: "+maban+" "+taobanmoi+" "+maNV+" "+mamon);
                if(taobanmoi.equals("true")){
                    String maDD = RandomStringUtils.randomNumeric(8);
                    createDonDat(maDD);
                    insertCTDD(maDD);
                    updateStatusTable(maban, maDD);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("taobanmoi","false");
                    editor.apply();
                }else{
                    String maDD = getMaDonDat(maban);
                    System.out.println("MAI XUAN Y 1: "+maban+" "+taobanmoi+" "+maNV+" "+mamon);
                    insertCTDD(maDD);
                    updateStatusTable(maban, maDD);
                    updateDonDat(maDD);
                }
                finish();
            }
        });
    }

    private void updateDonDat(String maDD) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DonDat");
        tongtien += Integer.parseInt(TXTL_amountmenu_SoLuong.getEditText().getText().toString().trim())*giatien;
        myRef.child(maDD).child("TongTien").setValue(tongtien);
    }

    @Nullable
    private String getMaDonDat(String maban) {
        System.out.println("size DonDat"+listDonDat.size());
        for(DonDat donDat: listDonDat){
            if(donDat.getMaBanAn().equals(maban)&&donDat.getTinhTrang()==0){
                tongtien = donDat.getTongTien();
                return donDat.getMaDonDat();
            }
        }
        return null;
    }

    private void insertCTDD(String maDD) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ChiTietDonDat");
        String maCTDD = String.valueOf(UUID.randomUUID());
        myRef.child(maCTDD).child("HinhAnh").setValue(hinhanh);
        myRef.child(maCTDD).child("MaMon").setValue(mamon);
        myRef.child(maCTDD).child("SoLuong").setValue(Integer.parseInt(TXTL_amountmenu_SoLuong.getEditText().getText().toString().trim()));
        myRef.child(maCTDD).child("TrangThai").setValue(0);
        myRef.child(maCTDD).child("MaDonDat").setValue(maDD);
        myRef.child(maCTDD).child("TenMon").setValue(tenmon);
        myRef.child(maCTDD).child("TongTien").setValue(Integer.parseInt(TXTL_amountmenu_SoLuong.getEditText().getText().toString().trim())*giatien);
    }

    private void updateStatusTable(String maban, String maDD) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("BanAn");
        myRef.child(maban).child("TrangThai").setValue(1);
        myRef.child(maban).child("MaDonDatHienTai").setValue(maDD);
    }

    private void createDonDat(String maDD){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DonDat");
        myRef.child(maDD).child("MaBanAn").setValue(maban);
        myRef.child(maDD).child("MaNV").setValue(maNV);
        String myFormat="dd-MM-yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        myRef.child(maDD).child("NgayDat").setValue(dateFormat.format(new Date()));
        myRef.child(maDD).child("TinhTrang").setValue(0);
        Long tongtien = 0L;
        int soluong = Integer.parseInt(TXTL_amountmenu_SoLuong.getEditText().getText().toString().trim());
        tongtien = soluong*giatien;
        myRef.child(maDD).child("TongTien").setValue(tongtien);
        myRef.child(maDD).child("HoVaTen").setValue(hoten);
        myRef.child(maDD).child("TenBan").setValue(tenban);
    }

    //validate số lượng
    private boolean validateAmount(){
        String val = TXTL_amountmenu_SoLuong.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            TXTL_amountmenu_SoLuong.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(!val.matches(("^[1-9]+[0-9]*$"))){
            TXTL_amountmenu_SoLuong.setError("Số lượng không hợp lệ");
            return false;
        }else {
            TXTL_amountmenu_SoLuong.setError(null);
            TXTL_amountmenu_SoLuong.setErrorEnabled(false);
            return true;
        }
    }
}