package com.sinhvien.orderdrinkapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinhvien.orderdrinkapp.CustomAdapter.AdapterDisplayPayment;
import com.sinhvien.orderdrinkapp.Data.BanAn;
import com.sinhvien.orderdrinkapp.Data.ChiTietDonDat;
import com.sinhvien.orderdrinkapp.Data.DonDat;
import com.sinhvien.orderdrinkapp.Data.Mon;
import com.sinhvien.orderdrinkapp.Data.ThanhToan;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;
import java.util.UUID;

public class PaymentActivity extends AppCompatActivity {

    ImageView IMG_payment_backbtn;
    TextView TXT_payment_TenBan, TXT_payment_NgayDat, TXT_payment_TongTien;
    Button BTN_payment_ThanhToan;
    ListView lvDisplayPayment;
    ArrayList<ThanhToan> data = new ArrayList<>();
    ArrayList<ChiTietDonDat> chiTietDonDatArrayList = new ArrayList<>();
    AdapterDisplayPayment adapterDisplayPayment;
    long tongtien = 0L;
    String maban, maDonDat;
    FragmentManager fragmentManager;
    boolean checkDoneAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_layout);
        setControl();
        getData();
        setEvent();
        fragmentManager = getSupportFragmentManager();
    }

    private void setEvent() {
        BTN_payment_ThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkDoneAll==false){
                    Toast.makeText(PaymentActivity.this, "Các món nước uống chưa được hoàn thiện", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("ThanhToan");
                for(ThanhToan thanhToan: data){
                    String maThanhToan = String.valueOf(UUID.randomUUID());
                    myRef.child(maThanhToan).child("MaDonDat").setValue(thanhToan.getMaDonDat());
                    myRef.child(maThanhToan).child("TenMon").setValue(thanhToan.getTenMon());
                    myRef.child(maThanhToan).child("MaMon").setValue(thanhToan.getMaMon());
                    myRef.child(maThanhToan).child("SoLuong").setValue(thanhToan.getSoLuong());
                    myRef.child(maThanhToan).child("GiaTien").setValue(thanhToan.getGiaTien());
                    myRef.child(maThanhToan).child("HinhAnh").setValue(thanhToan.getHinhAnh());
                }
                DatabaseReference myRef2 = database.getReference("DonDat");
                myRef2.child(maDonDat).child("TinhTrang").setValue(1);
                DatabaseReference myRef3 = database.getReference("BanAn");
                myRef3.child(maban).child("TrangThai").setValue(0);
                myRef3.child(maban).child("MaDonDatHienTai").setValue("null");

                finish();
            }
        });

        IMG_payment_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getData() {
        HienThiThanhToan();
        //lấy data từ mã bàn đc chọn
        Intent intent = getIntent();
        maban = intent.getStringExtra("maban");
        String tenban = intent.getStringExtra("tenban");
        TXT_payment_TenBan.setText(tenban);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef3 = database.getReference("BanAn");
        myRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item: snapshot.getChildren()){
                    BanAn a = item.getValue(BanAn.class);
                    a.setMaBanAn(item.getKey());
                    if(a.getMaBanAn()!=null){
                        if(a.getMaBanAn().equals(maban)) maDonDat = a.getMaDonDatHienTai();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference myRef2 = database.getReference("DonDat");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item: snapshot.getChildren()){
                    DonDat a = item.getValue(DonDat.class);
                    a.setMaDonDat(item.getKey());
                    if(a.getMaDonDat()!=null){
                        if(a.getMaDonDat().equals(maDonDat)) {
                            System.out.println("Ma Don Dat: "+a.getMaDonDat());
                            TXT_payment_NgayDat.setText(a.getNgayDat());
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference myRef = database.getReference("ChiTietDonDat");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chiTietDonDatArrayList.clear();
                checkDoneAll = true;
                for(DataSnapshot item: snapshot.getChildren()){
                    ChiTietDonDat a = item.getValue(ChiTietDonDat.class);
                    a.setMaCTDD(item.getKey());
                    if(a.getMaDonDat()!=null){
                        if(a.getMaDonDat().equals(maDonDat)) {
                            if(a.getTrangThai()==0) checkDoneAll = false;
                            chiTietDonDatArrayList.add(a);
                            ThanhToan thanhToan = new ThanhToan();
                            thanhToan.setMaDonDat(maDonDat);
                            thanhToan.setMaMon(a.getMaMon());
                            thanhToan.setSoLuong(a.getSoLuong());
                            thanhToan.setTenMon(a.getTenMon());
                            thanhToan.setHinhAnh(a.getHinhAnh());
                            thanhToan.setGiaTien(a.getTongTien());
                            Integer vitri = checkTonTai(data, thanhToan);
                            if(vitri==null) data.add(thanhToan);
                            else{
                                ThanhToan temp = data.get(vitri);
                                data.get(vitri).setSoLuong(thanhToan.getSoLuong()+temp.getSoLuong());
                                data.get(vitri).setGiaTien(thanhToan.getGiaTien()+temp.getGiaTien());
                            }
                        }

                    }
                }
                adapterDisplayPayment.notifyDataSetChanged();
                setTongTien();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
            Integer checkTonTai(ArrayList<ThanhToan> list, ThanhToan thanhToan){
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getMaMon().equals(thanhToan.getMaMon())) return i;
                }
                return null;
            }
        });

    }

    private void setControl() {
        //region thuộc tính view
        lvDisplayPayment= findViewById(R.id.lvDisplayPayment);
        IMG_payment_backbtn = findViewById(R.id.img_payment_backbtn);
        TXT_payment_TenBan = findViewById(R.id.txt_payment_TenBan);
        TXT_payment_NgayDat = findViewById(R.id.txt_payment_NgayDat);
        TXT_payment_TongTien = findViewById(R.id.txt_payment_TongTien);
        BTN_payment_ThanhToan = findViewById(R.id.btn_payment_ThanhToan);
        //endregion
    }

    private void setTongTien() {
        tongtien = 0;
        for (int i=0;i<data.size();i++){
            if(data.get(i).getGiaTien()!=null) {
                tongtien += data.get(i).getGiaTien();
            }
        }
        TXT_payment_TongTien.setText(String.valueOf(tongtien) +" VNĐ");
    }

    //hiển thị data lên listView
    private void HienThiThanhToan(){
        adapterDisplayPayment = new AdapterDisplayPayment(PaymentActivity.this,R.layout.custom_layout_paymentmenu,data);
        lvDisplayPayment.setAdapter(adapterDisplayPayment);
    }
}