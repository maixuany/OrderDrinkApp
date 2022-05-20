package com.sinhvien.orderdrinkapp.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.sinhvien.orderdrinkapp.Data.BanAn;
import com.sinhvien.orderdrinkapp.Data.DonDat;
import com.sinhvien.orderdrinkapp.Data.NhanVien;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;

public class AdapterDisplayStatistic extends ArrayAdapter<DonDat> {

    Context context;
    int resource;
    ArrayList<DonDat> listDonDat;

    public AdapterDisplayStatistic(@NonNull Context context, int resource,@NonNull ArrayList<DonDat> listDonDat) {
        super(context, resource, listDonDat);
        this.context = context;
        this.resource = resource;
        this.listDonDat = listDonDat;
    }

    @Override
    public int getCount() {
        return listDonDat.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource,null);
        DonDat donDat = listDonDat.get(position);
        TextView txt_customstatistic_MaDon = convertView.findViewById(R.id.txt_customstatistic_MaDon);
        TextView txt_customstatistic_NgayDat = convertView.findViewById(R.id.txt_customstatistic_NgayDat);
        TextView txt_customstatistic_TenNV = convertView.findViewById(R.id.txt_customstatistic_TenNV);
        TextView txt_customstatistic_TongTien = convertView.findViewById(R.id.txt_customstatistic_TongTien);
        TextView txt_customstatistic_TrangThai = convertView.findViewById(R.id.txt_customstatistic_TrangThai);
        TextView txt_customstatistic_BanDat = convertView.findViewById(R.id.txt_customstatistic_BanDat);
        txt_customstatistic_TongTien.setText(donDat.getTongTien()+"");
        txt_customstatistic_NgayDat.setText(donDat.getNgayDat());
        if(donDat.getTinhTrang()==0)
            txt_customstatistic_TrangThai.setText("Chưa Thanh Toán");
        else
            txt_customstatistic_TrangThai.setText("Đã Thanh Toán");
        txt_customstatistic_MaDon.setText(donDat.getMaDonDat());
        txt_customstatistic_TenNV.setText(donDat.getHoVaTen());
        txt_customstatistic_BanDat.setText(donDat.getTenBan());
        return convertView;
    }

}
