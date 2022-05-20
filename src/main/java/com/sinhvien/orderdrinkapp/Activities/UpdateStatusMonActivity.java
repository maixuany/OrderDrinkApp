package com.sinhvien.orderdrinkapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.sinhvien.orderdrinkapp.CustomAdapter.AdapterDisplayStatusMon;
import com.sinhvien.orderdrinkapp.Data.BanAn;
import com.sinhvien.orderdrinkapp.Data.ChiTietDonDat;
import com.sinhvien.orderdrinkapp.Data.DonDat;
import com.sinhvien.orderdrinkapp.Data.Mon;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;

public class UpdateStatusMonActivity extends AppCompatActivity {
    TextView txt_statusmon_TenBan, txt_statusmon_NgayDat;
    ListView lvDisplayStatusMon;
    Button btn_statusmon_doneall;
    ImageView img_statusmon_backbtn;
    String maban, tenban;
    String maDonDat;
    Long tongtien = 0L;
    String role;
    AdapterDisplayStatusMon adapterDisplayStatusMon;
    ArrayList<ChiTietDonDat> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status_mon);
        setControl();
        getData();
        setEvent();
    }

    private void getData() {
        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        role = sharedPreferences.getString("role","");
        if(!role.equals("PC")){
            registerForContextMenu(lvDisplayStatusMon);
        }
        maban = getIntent().getStringExtra("maban");
        tenban = getIntent().getStringExtra("tenban");
        maDonDat = getIntent().getStringExtra("maDonDat");
        txt_statusmon_TenBan.setText(tenban);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 = database.getReference("DonDat");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item: snapshot.getChildren()){
                    DonDat a = item.getValue(DonDat.class);
                    a.setMaDonDat(item.getKey());
                    if(a.getMaDonDat()!=null){
                        if(a.getMaDonDat().equals(maDonDat)) {
                            tongtien = a.getTongTien();
                            System.out.println("Ma Don Dat: "+a.getMaDonDat());
                            txt_statusmon_NgayDat.setText(a.getNgayDat());
                        }
                    }
                    System.out.println("TỔNG TIỀN TRONG DATA: "+ tongtien);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference myRef = database.getReference("ChiTietDonDat");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for(DataSnapshot item: snapshot.getChildren()){
                    ChiTietDonDat a = item.getValue(ChiTietDonDat.class);
                    a.setMaCTDD(item.getKey());
                    if(a.getMaDonDat()!=null){
                        if(a.getMaDonDat().equals(maDonDat)) {
                            data.add(a);
                        }
                    }
                }
                adapterDisplayStatusMon.notifyDataSetChanged();
                if(checkDoneAll()==1){
                    DatabaseReference ref = database.getReference("BanAn");
                    ref.child(maban).child("TrangThai").setValue(2);
                }else if(checkDoneAll()==0){
                    DatabaseReference ref = database.getReference("BanAn");
                    ref.child(maban).child("TrangThai").setValue(1);
                }else{
                    DatabaseReference ref = database.getReference("BanAn");
                    ref.child(maban).child("TrangThai").setValue(0);
                    ref.child(maban).child("MaDonDatHienTai").setValue("null");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            int checkDoneAll(){
                if(data.size()==0) return 2;
                for(ChiTietDonDat chiTietDonDat: data){
                    if(chiTietDonDat.getTrangThai()==0) return 0;
                }
                return 1;
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    private void setEvent() {
        adapterDisplayStatusMon = new AdapterDisplayStatusMon(this, R.layout.custom_layout_status_mon, data);
        lvDisplayStatusMon.setAdapter(adapterDisplayStatusMon);
        img_statusmon_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_statusmon_doneall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!role.equals("NV")){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("ChiTietDonDat");
                    for(ChiTietDonDat chiTietDonDat: data){
                        myRef.child(chiTietDonDat.getMaCTDD()).child("TrangThai").setValue(1);
                    }
                    DatabaseReference ref = database.getReference("BanAn");
                    ref.child(maban).child("TrangThai").setValue(2);
                    getData();
                    Toast.makeText(UpdateStatusMonActivity.this, "Đã hoàn thành tất cả các món", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Bạn không có quyền truy cập",Toast.LENGTH_SHORT).show();
                }
            }
        });

        lvDisplayStatusMon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!role.equals("NV")){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("ChiTietDonDat");
                    myRef.child(data.get(i).getMaCTDD()).child("TrangThai").setValue(1);
                    getData();
                }else{
                    Toast.makeText(getApplicationContext(),"Bạn không có quyền truy cập",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        this.getMenuInflater().inflate(R.menu.delete_context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;

        switch (id){
            case R.id.itDelete:
                if(data.get(vitri).getTrangThai()!=0){
                    Toast.makeText(UpdateStatusMonActivity.this, "Không thể xóa món đã pha chế",Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    Long tien = data.get(vitri).getTongTien();
                    DatabaseReference myRef1 = database.getReference("DonDat");
                    System.out.println("TỔNG TIỀN: "+tongtien);
                    myRef1.child(maDonDat).child("TongTien").setValue(tongtien-tien);
                    DatabaseReference myRef = database.getReference("ChiTietDonDat");
                    myRef.child(data.get(vitri).getMaCTDD()).removeValue();
                    Toast.makeText(UpdateStatusMonActivity.this, "Xóa thành công",Toast.LENGTH_SHORT).show();
                    if((tongtien-tien)==0){
                        myRef1.child(maDonDat).removeValue();
                        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(maban,null);
                        editor.apply();
                        DatabaseReference myRef3 = database.getReference("BanAn");
                        myRef3.child(maban).child("TrangThai").setValue(0);
                    }
                }
                getData();
                break;
        }

        return true;
    }

    private void setControl() {
        txt_statusmon_TenBan = findViewById(R.id.txt_statusmon_TenBan);
        txt_statusmon_NgayDat = findViewById(R.id.txt_statusmon_NgayDat);
        lvDisplayStatusMon = findViewById(R.id.lvDisplayStatusMon);
        btn_statusmon_doneall = findViewById(R.id.btn_statusmon_doneall);
        img_statusmon_backbtn = findViewById(R.id.img_statusmon_backbtn);
    }
}