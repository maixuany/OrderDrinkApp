package com.sinhvien.orderdrinkapp.CustomAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.sinhvien.orderdrinkapp.Data.LoaiMon;
import com.sinhvien.orderdrinkapp.R;

import java.util.Base64;
import java.util.List;

public class AdapterRecycleViewCategory extends RecyclerView.Adapter<AdapterRecycleViewCategory.ViewHolder>{

    Context context;
    int layout;
    List<LoaiMon> data;

    public AdapterRecycleViewCategory(Context context,int layout, List<LoaiMon> data){
        this.context = context;
        this.layout = layout;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(AdapterRecycleViewCategory.ViewHolder holder, int position) {
        LoaiMon loaiMon = data.get(position);
        holder.txt_customcategory_TenLoai.setText(loaiMon.getTenLoai());
        try{
            byte[] categoryimage = stringToBytes(loaiMon.getHinhAnh());
            Bitmap bitmap = BitmapFactory.decodeByteArray(categoryimage,0,categoryimage.length);
            holder.img_customcategory_HinhLoai.setImageBitmap(bitmap);
        }catch (Exception e){

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private byte[] stringToBytes(String a){
        return Base64.getDecoder().decode(a);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txt_customcategory_TenLoai;
        ImageView img_customcategory_HinhLoai;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            txt_customcategory_TenLoai = itemView.findViewById(R.id.txt_customcategory_TenLoai);
            img_customcategory_HinhLoai = itemView.findViewById(R.id.img_customcategory_HinhLoai);
        }
    }

}
