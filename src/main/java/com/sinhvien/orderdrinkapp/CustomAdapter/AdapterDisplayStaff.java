package com.sinhvien.orderdrinkapp.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.sinhvien.orderdrinkapp.Data.NhanVien;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;

public class AdapterDisplayStaff extends ArrayAdapter<NhanVien> {

    Context context;
    int resource;
    ArrayList<NhanVien> data;

    public AdapterDisplayStaff(@NonNull Context context, int resource, @NonNull ArrayList<NhanVien> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource,null);
        ImageView img_customstaff_HinhNV = convertView.findViewById(R.id.img_customstaff_HinhNV);
        TextView txt_customstaff_TenNV = convertView.findViewById(R.id.txt_customstaff_TenNV);
        TextView txt_customstaff_TenQuyen = convertView.findViewById(R.id.txt_customstaff_TenQuyen);
        TextView txt_customstaff_SDT = convertView.findViewById(R.id.txt_customstaff_SDT);
        TextView txt_customstaff_Email  = convertView.findViewById(R.id.txt_customstaff_Email);
        NhanVien nhanVien = data.get(position);
        txt_customstaff_TenNV.setText(nhanVien.getHoVaTen());
        if(nhanVien.getMaQuyen().equals("QL"))
            txt_customstaff_TenQuyen.setText("Quản Lý");
        if(nhanVien.getMaQuyen().equals("NV"))
            txt_customstaff_TenQuyen.setText("Nhân Viên");
        if(nhanVien.getMaQuyen().equals("TN"))
            txt_customstaff_TenQuyen.setText("Thu Ngân");
        if(nhanVien.getMaQuyen().equals("PC"))
            txt_customstaff_TenQuyen.setText("Pha Chế");
        txt_customstaff_SDT.setText(nhanVien.getSDT());
        txt_customstaff_Email.setText(nhanVien.getEmail());
        return  convertView;
    }

}
