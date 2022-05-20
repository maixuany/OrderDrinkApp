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
import com.sinhvien.orderdrinkapp.Data.ChiTietDonDat;
import com.sinhvien.orderdrinkapp.Data.Mon;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;
import java.util.Base64;

public class AdapterDisplayStatusMon extends ArrayAdapter<ChiTietDonDat> {
    Context context;
    int resource;
    ArrayList<ChiTietDonDat> data;

    public AdapterDisplayStatusMon(@NonNull Context context, int resource, @NonNull ArrayList<ChiTietDonDat> data) {
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
        ImageView img_customstatus_HinhMon = convertView.findViewById(R.id.img_customstatus_HinhMon);
        TextView txt_customstatus_TenMon = convertView.findViewById(R.id.txt_customstatus_TenMon);
        TextView txt_customstatus_SoLuong = convertView.findViewById(R.id.txt_customstatus_SoLuong);
        TextView txt_customstatus_update = convertView.findViewById(R.id.txt_customstatus_update);
        ChiTietDonDat chiTietDonDat = data.get(position);
        try{
            byte[] categoryimage = stringToBytes(chiTietDonDat.getHinhAnh());
            txt_customstatus_TenMon.setText(chiTietDonDat.getTenMon());
            img_customstatus_HinhMon.setImageBitmap(BitmapFactory.decodeByteArray(categoryimage, 0, categoryimage.length));
        }catch (Exception e){

        }
        txt_customstatus_SoLuong.setText(chiTietDonDat.getSoLuong()+"");
        if(chiTietDonDat.getTrangThai()==1) txt_customstatus_update.setText("Đã xong");
        else txt_customstatus_update.setText("Đang làm");
        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private byte[] stringToBytes(String a){
        return Base64.getDecoder().decode(a);
    }

}
