package com.sinhvien.orderdrinkapp.CustomAdapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.sinhvien.orderdrinkapp.Data.ThanhToan;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AdapterDisplayPayment extends ArrayAdapter<ThanhToan> {

    Context context;
    int resource;
    List<ThanhToan> data;

    public AdapterDisplayPayment(@NonNull Context context, int resource, @NonNull ArrayList<ThanhToan> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource,null);
        ImageView img_custompayment_HinhMon = convertView.findViewById(R.id.img_custompayment_HinhMon);
        TextView txt_custompayment_TenMon = convertView.findViewById(R.id.txt_custompayment_TenMon);
        TextView txt_custompayment_SoLuong = convertView.findViewById(R.id.txt_custompayment_SoLuong);
        TextView txt_custompayment_GiaTien = convertView.findViewById(R.id.txt_custompayment_GiaTien);
        ThanhToan thanhToan = data.get(position);
        txt_custompayment_TenMon.setText(thanhToan.getTenMon());
        txt_custompayment_SoLuong.setText(thanhToan.getSoLuong()+"");
        txt_custompayment_GiaTien.setText(thanhToan.getGiaTien()+"");
        try{
            byte[] image = stringToBytes(thanhToan.getHinhAnh());
            img_custompayment_HinhMon.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        }catch (Exception e){

        }
        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private byte[] stringToBytes(String a){
        return Base64.getDecoder().decode(a);
    }
}
