package com.sinhvien.orderdrinkapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinhvien.orderdrinkapp.Activities.AddCategoryActivity;
import com.sinhvien.orderdrinkapp.Activities.HomeActivity;
import com.sinhvien.orderdrinkapp.CustomAdapter.AdapterRecycleViewCategory;
import com.sinhvien.orderdrinkapp.CustomAdapter.AdapterRecycleViewStatistic;
import com.sinhvien.orderdrinkapp.Data.BanAn;
import com.sinhvien.orderdrinkapp.Data.DonDat;
import com.sinhvien.orderdrinkapp.Data.LoaiMon;
import com.sinhvien.orderdrinkapp.Data.NhanVien;
import com.sinhvien.orderdrinkapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DisplayHomeFragment extends Fragment {

    RecyclerView rcv_displayhome_LoaiMon, rcv_displayhome_DonTrongNgay;
    RelativeLayout layout_displayhome_ThongKe,layout_displayhome_XemBan, layout_displayhome_XemMenu, layout_displayhome_XemNV;
    TextView txt_displayhome_ViewAllCategory, txt_displayhome_ViewAllStatistic;
    ArrayList<DonDat> listDonDat = new ArrayList<>();
    ArrayList<LoaiMon> listLM = new ArrayList<>();
    String role = "";
    AdapterRecycleViewCategory adapterRecycleViewCategory;
    AdapterRecycleViewStatistic adapterRecycleViewStatistic;
    View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.displayhome_layout,container,false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Trang chủ");
        setHasOptionsMenu(true);
        setControl();
        getData();
        setEvent();
        return view;
    }

    private void setControl() {
        //region Lấy dối tượng view
        rcv_displayhome_LoaiMon = (RecyclerView)view.findViewById(R.id.rcv_displayhome_LoaiMon);
        rcv_displayhome_DonTrongNgay = (RecyclerView)view.findViewById(R.id.rcv_displayhome_DonTrongNgay);
        layout_displayhome_ThongKe = (RelativeLayout)view.findViewById(R.id.layout_displayhome_ThongKe);
        layout_displayhome_XemBan = (RelativeLayout)view.findViewById(R.id.layout_displayhome_XemBan);
        layout_displayhome_XemMenu = (RelativeLayout)view.findViewById(R.id.layout_displayhome_XemMenu);
        layout_displayhome_XemNV = (RelativeLayout)view.findViewById(R.id.layout_displayhome_XemNV);
        txt_displayhome_ViewAllCategory = (TextView) view.findViewById(R.id.txt_displayhome_ViewAllCategory);
        txt_displayhome_ViewAllStatistic = (TextView) view.findViewById(R.id.txt_displayhome_ViewAllStatistic);
        //endregion
    }

    private void setEvent() {
        HienThiDSLoai();
        HienThiDonTrongNgay();
        NavigationView navigationView = (NavigationView)getActivity().findViewById(R.id.navigation_view_trangchu);
        layout_displayhome_ThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction tranDisplayStatistic = getActivity().getSupportFragmentManager().beginTransaction();
                tranDisplayStatistic.replace(R.id.contentView,new DisplayStatisticFragment());
                tranDisplayStatistic.addToBackStack(null);
                tranDisplayStatistic.commit();
                navigationView.setCheckedItem(R.id.nav_statistic);
            }
        });

        txt_displayhome_ViewAllStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction tranDisplayStatistic = getActivity().getSupportFragmentManager().beginTransaction();
                tranDisplayStatistic.replace(R.id.contentView,new DisplayStatisticFragment());
                tranDisplayStatistic.addToBackStack(null);
                tranDisplayStatistic.commit();
                navigationView.setCheckedItem(R.id.nav_statistic);
            }
        });

        layout_displayhome_XemBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction tranDisplayTable = getActivity().getSupportFragmentManager().beginTransaction();
                tranDisplayTable.replace(R.id.contentView,new DisplayTableFragment());
                tranDisplayTable.addToBackStack(null);
                tranDisplayTable.commit();
                navigationView.setCheckedItem(R.id.nav_table);
            }
        });

        layout_displayhome_XemMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(role.equals("QL")){
                    Intent iAddCategory = new Intent(getActivity(), AddCategoryActivity.class);
                    iAddCategory.putExtra("ID","ADD");
                    startActivity(iAddCategory);
                    navigationView.setCheckedItem(R.id.nav_category);
                }else{
                    Toast.makeText(getContext(),"Bạn không có quyền truy cập",Toast.LENGTH_SHORT).show();
                }
            }
        });

        layout_displayhome_XemNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("session", getActivity().MODE_PRIVATE);
                String role = sharedPreferences.getString("role","");
                if(role.equals("QL")) {
                    FragmentTransaction tranDisplayStaff = getActivity().getSupportFragmentManager().beginTransaction();
                    tranDisplayStaff.replace(R.id.contentView, new DisplayStaffFragment());
                    tranDisplayStaff.addToBackStack(null);
                    tranDisplayStaff.commit();
                    navigationView.setCheckedItem(R.id.nav_staff);
                }else{
                    Toast.makeText(getContext(),"Bạn không có quyền truy cập",Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_displayhome_ViewAllCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("session", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("maban", null);
                editor.putString("taobanmoi", null);
                editor.apply();
                FragmentTransaction tranDisplayCategory = getActivity().getSupportFragmentManager().beginTransaction();
                tranDisplayCategory.replace(R.id.contentView,new DisplayCategoryFragment());
                tranDisplayCategory.addToBackStack(null);
                tranDisplayCategory.commit();
                navigationView.setCheckedItem(R.id.nav_category);
            }
        });
    }

    private void getData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("session", getActivity().MODE_PRIVATE);
        role = sharedPreferences.getString("role","");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("LoaiMon");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listLM.clear();
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    LoaiMon a = item.getValue(LoaiMon.class);
                    a.setMaLoaiMon(item.getKey());
                    listLM.add(a);
                }
                adapterRecycleViewCategory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        String myFormat="dd-MM-yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        String date = dateFormat.format(new Date());
        DatabaseReference myRef3 = database.getReference("DonDat");
        myRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listDonDat.clear();
                for(DataSnapshot item: snapshot.getChildren()){
                    DonDat a = item.getValue(DonDat.class);
                    a.setMaDonDat(item.getKey());
                    if(a.getNgayDat()!=null){
                        if(a.getNgayDat().equals(date)) {
                            listDonDat.add(a);
                        }
                    }
                }
                adapterRecycleViewStatistic.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void HienThiDSLoai(){
        rcv_displayhome_LoaiMon.setHasFixedSize(true);
        rcv_displayhome_LoaiMon.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        adapterRecycleViewCategory = new AdapterRecycleViewCategory(getActivity(),R.layout.custom_layout_displaycategory,listLM);
        rcv_displayhome_LoaiMon.setAdapter(adapterRecycleViewCategory);
        adapterRecycleViewCategory.notifyDataSetChanged();
    }

    private void HienThiDonTrongNgay(){
        rcv_displayhome_DonTrongNgay.setHasFixedSize(true);
        rcv_displayhome_DonTrongNgay.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        adapterRecycleViewStatistic = new AdapterRecycleViewStatistic(getActivity(),R.layout.custom_layout_displaystatistic,listDonDat);
        rcv_displayhome_DonTrongNgay.setAdapter(adapterRecycleViewStatistic);
        adapterRecycleViewCategory.notifyDataSetChanged();
    }

}

