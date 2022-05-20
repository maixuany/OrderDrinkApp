package com.sinhvien.orderdrinkapp.CustomAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sinhvien.orderdrinkapp.Activities.HomeActivity;
import com.sinhvien.orderdrinkapp.Activities.PaymentActivity;
import com.sinhvien.orderdrinkapp.Activities.UpdateStatusMonActivity;
import com.sinhvien.orderdrinkapp.Data.BanAn;
import com.sinhvien.orderdrinkapp.Fragments.DisplayCategoryFragment;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;

public class AdapterDisplayTable extends ArrayAdapter<BanAn>{

    Context context;
    int resource;
    ArrayList<BanAn> listBanAn;
    ImageView imgBanAn, imgGoiMon, imgThanhToan, imgTrangThaiMon;
    TextView txtTenBanAn;
    FragmentManager fragmentManager;
    String role = "";

    public AdapterDisplayTable(@NonNull Context context, int resource, @NonNull ArrayList<BanAn> listBanAn) {
        super(context, resource, listBanAn);
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", context.MODE_PRIVATE);
        role = sharedPreferences.getString("role","");
        this.context = context;
        this.resource = resource;
        this.listBanAn = listBanAn;
        fragmentManager = ((HomeActivity)context).getSupportFragmentManager();
    }


    @Override
    public int getCount() {
        return listBanAn.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource,null);
        imgBanAn = convertView.findViewById(R.id.img_customtable_BanAn);
        imgGoiMon = convertView.findViewById(R.id.img_customtable_GoiMon);
        imgThanhToan = convertView.findViewById(R.id.img_customtable_ThanhToan);
        imgTrangThaiMon = convertView.findViewById(R.id.img_customtable_TrangThai);
        txtTenBanAn = convertView.findViewById(R.id.txt_customtable_TenBanAn);
        txtTenBanAn.setText(listBanAn.get(position).getTenBanAn());
        if(listBanAn.get(position).getTrangThai()==1) imgBanAn.setImageResource(R.drawable.ic_baseline_event_seated_40);
        else if(listBanAn.get(position).getTrangThai()==0||listBanAn.get(position).getMaDonDatHienTai().equals("null")) imgBanAn.setImageResource(R.drawable.ic_baseline_event_seat_40);
        else imgBanAn.setImageResource(R.drawable.ic_baseline_event_doneall_40);
        imgGoiMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(role.equals("NV")||role.equals("QL")){
                    Intent getIHome = ((HomeActivity)context).getIntent();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    DisplayCategoryFragment displayCategoryFragment = new DisplayCategoryFragment();
                    Bundle bDataCategory = new Bundle();
                    if(listBanAn.get(position).getTrangThai()==0||listBanAn.get(position).getMaDonDatHienTai().equals("null")){
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("session", getContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("maban", listBanAn.get(position).getMaBanAn());
                        editor.putString("taobanmoi", "true");
                        editor.putString("tenban", listBanAn.get(position).getTenBanAn());
                        editor.apply();
                    }else{
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("session", getContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("maban", listBanAn.get(position).getMaBanAn());
                        editor.putString("taobanmoi", "false");
                        editor.putString("tenban", listBanAn.get(position).getTenBanAn());
                        editor.apply();
                    }
                    displayCategoryFragment.setArguments(bDataCategory);
                    transaction.replace(R.id.contentView,displayCategoryFragment).addToBackStack("hienthibanan");
                    transaction.commit();
                }else{
                    Toast.makeText(context,"Bạn không có quyền truy cập",Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(role.equals("QL")||role.equals("TN")){
                    if(listBanAn.get(position).getTrangThai()==0||listBanAn.get(position).getMaDonDatHienTai().equals("null")){
                        Toast.makeText(context, "Bàn chưa có người ngồi", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent iThanhToan = new Intent(context, PaymentActivity.class);
                        iThanhToan.putExtra("maban",listBanAn.get(position).getMaBanAn());
                        iThanhToan.putExtra("tenban",listBanAn.get(position).getTenBanAn());
                        context.startActivity(iThanhToan);
                    }
                }else{
                    Toast.makeText(context,"Bạn không có quyền truy cập",Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgTrangThaiMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(role.equals("PC")||role.equals("QL")||role.equals("NV")){
                    if(listBanAn.get(position).getTrangThai()==0||listBanAn.get(position).getMaDonDatHienTai().equals("null")){
                        Toast.makeText(context, "Bàn Chưa Có Người Ngồi", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent iTrangthai = new Intent(context, UpdateStatusMonActivity.class);
                        iTrangthai.putExtra("maban", listBanAn.get(position).getMaBanAn());
                        iTrangthai.putExtra("tenban", listBanAn.get(position).getTenBanAn());
                        iTrangthai.putExtra("maDonDat", listBanAn.get(position).getMaDonDatHienTai());
                        context.startActivity(iTrangthai);
                    }
                }else{
                    Toast.makeText(context,"Bạn không có quyền truy cập",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return convertView;
    }

}
