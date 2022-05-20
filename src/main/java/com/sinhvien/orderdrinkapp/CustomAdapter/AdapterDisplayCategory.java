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

import com.sinhvien.orderdrinkapp.Data.LoaiMon;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;
import java.util.Base64;

public class AdapterDisplayCategory extends ArrayAdapter<LoaiMon> {

    Context context;
    int layout;
    ArrayList<LoaiMon> data;
    TextView txt_customcategory_TenLoai;
    ImageView img_customcategory_HinhLoai;

    public AdapterDisplayCategory(@NonNull Context context, int resource, @NonNull ArrayList<LoaiMon> data) {
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
        txt_customcategory_TenLoai = convertView.findViewById(R.id.txt_customcategory_TenLoai);
        img_customcategory_HinhLoai = convertView.findViewById(R.id.img_customcategory_HinhLoai);
        txt_customcategory_TenLoai.setText(data.get(position).getTenLoai());
        try{
            byte[] categoryimage = stringToBytes(data.get(position).getHinhAnh());
            img_customcategory_HinhLoai.setImageBitmap(BitmapFactory.decodeByteArray(categoryimage,0,categoryimage.length));
        }catch (Exception e){

        }
        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private byte[] stringToBytes(String a){
        return Base64.getDecoder().decode(a);
    }

}
