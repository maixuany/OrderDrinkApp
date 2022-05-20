package com.sinhvien.orderdrinkapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinhvien.orderdrinkapp.Activities.DetailStatisticActivity;
import com.sinhvien.orderdrinkapp.Activities.HomeActivity;
import com.sinhvien.orderdrinkapp.Activities.StaticThongKe;
import com.sinhvien.orderdrinkapp.CustomAdapter.AdapterDisplayStatistic;
import com.sinhvien.orderdrinkapp.Data.BanAn;
import com.sinhvien.orderdrinkapp.Data.DonDat;
import com.sinhvien.orderdrinkapp.Data.NhanVien;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DisplayStatisticFragment extends Fragment {

    ListView lvStatistic;
    ArrayList<DonDat> listDonDat = new ArrayList<>();
    ArrayList<NhanVien> listNhanVien = new ArrayList<>();
    ArrayList<BanAn> listBanAn = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    AdapterDisplayStatistic adapterDisplayStatistic;
    FragmentManager fragmentManager;
    View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.displaystatistic_layout,container,false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Quản lý thống kê");
        setHasOptionsMenu(true);
        setControl();
        getData();
        setEvent();
        return view;
    }

    private void setEvent() {
        floatingActionButton.setOnClickListener(views->{
            Intent intent = new Intent(getContext(), StaticThongKe.class);
            startActivity(intent);
        });

        lvStatistic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DonDat donDat = listDonDat.get(i);
                if(donDat.getTinhTrang()==0){
                    Toast.makeText(getContext(), "Đơn Chưa Được Thanh Toán", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), DetailStatisticActivity.class);
                intent.putExtra("madon", donDat.getMaDonDat());
                intent.putExtra("tenban", donDat.getTenBan());
                intent.putExtra("tenNV", donDat.getHoVaTen());
                intent.putExtra("tongtien", donDat.getTongTien());
                intent.putExtra("ngaydat", donDat.getNgayDat());
                startActivity(intent);
            }
        });
    }

    private void getData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef3 = database.getReference("DonDat");
        myRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listDonDat.clear();
                for(DataSnapshot item: snapshot.getChildren()){
                    DonDat a = item.getValue(DonDat.class);
                    a.setMaDonDat(item.getKey());
                    listDonDat.add(a);
                }
                SapXepTheoNgay(listDonDat);
                adapterDisplayStatistic.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void SapXepTheoNgay(ArrayList<DonDat> list){
        for(int i=0;i<list.size()-1;i++){
            for(int j=i+1;j<list.size();j++){
                if(list.get(i).getNgayDat().compareTo(list.get(j).getNgayDat())>0){
                    Collections.swap(list, i, j);
                }
            }
        }
    }

    private void setControl() {
        lvStatistic = (ListView)view.findViewById(R.id.lvStatistic);
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.btnstatic) ;
        adapterDisplayStatistic = new AdapterDisplayStatistic(getActivity(),R.layout.custom_layout_displaystatistic,listDonDat);
        lvStatistic.setAdapter(adapterDisplayStatistic);
        adapterDisplayStatistic.notifyDataSetChanged();
    }
}
