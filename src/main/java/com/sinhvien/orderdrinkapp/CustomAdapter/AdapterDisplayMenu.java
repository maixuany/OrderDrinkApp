package com.sinhvien.orderdrinkapp.CustomAdapter;

import android.content.Context;
import android.graphics.Bitmap;
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

import com.sinhvien.orderdrinkapp.Data.Mon;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;
import java.util.Base64;

public class AdapterDisplayMenu extends ArrayAdapter<Mon> {

    Context context;
    int layout;
    ArrayList<Mon> data;

    public AdapterDisplayMenu(@NonNull Context context, int resource, @NonNull ArrayList<Mon> data) {
        super(context, resource, data);
        this.context = context;
        this.layout = resource;
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
        convertView = LayoutInflater.from(context).inflate(layout,null);
        ImageView img_custommenu_HinhMon = convertView.findViewById(R.id.img_custommenu_HinhMon);
        TextView txt_custommenu_TenMon = convertView.findViewById(R.id.txt_custommenu_TenMon);
        TextView txt_custommenu_TinhTrang = convertView.findViewById(R.id.txt_custommenu_TinhTrang);
        TextView txt_custommenu_GiaTien = convertView.findViewById(R.id.txt_custommenu_GiaTien);
        txt_custommenu_TenMon.setText(data.get(position).getTenMon());
        txt_custommenu_GiaTien.setText(data.get(position).getGiaTien()+" VND");
        if(data.get(position).getTinhTrang()==0)
            txt_custommenu_TinhTrang.setText("Hết Món");
        else txt_custommenu_TinhTrang.setText("Còn Món");
        try{
            byte[] menuImage = stringToBytes(data.get(position).getHinhAnh());
            Bitmap bitmap = BitmapFactory.decodeByteArray(menuImage,0,menuImage.length);
            img_custommenu_HinhMon.setImageBitmap(bitmap);
        }catch (Exception e){

        }
        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private byte[] stringToBytes(String a){
        return Base64.getDecoder().decode(a);
    }

}
