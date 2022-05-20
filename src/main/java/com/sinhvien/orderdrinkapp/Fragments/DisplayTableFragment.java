package com.sinhvien.orderdrinkapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinhvien.orderdrinkapp.Activities.AddTableActivity;
import com.sinhvien.orderdrinkapp.Activities.EditTableActivity;
import com.sinhvien.orderdrinkapp.Activities.HomeActivity;
import com.sinhvien.orderdrinkapp.CustomAdapter.AdapterDisplayTable;
import com.sinhvien.orderdrinkapp.Data.BanAn;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class DisplayTableFragment extends Fragment {

    GridView GVDisplayTable;
    AdapterDisplayTable adapterDisplayTable;
    ArrayList<BanAn> listBA = new ArrayList<>();
    String role = "";

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.displaytable_layout,container,false);
        setHasOptionsMenu(true);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Quản lý bàn");
        GVDisplayTable = (GridView)view.findViewById(R.id.gvDisplayTable);
        getData();
        HienThiDSBan();
        // nếu role là Quản lý thì nhấn giữ bàn -> hiển thị menu sửa xóa
        if(role.equals("QL")){
            registerForContextMenu(GVDisplayTable);
        }
        return view;
    }

    private void getData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("session", getContext().MODE_PRIVATE);
        role = sharedPreferences.getString("role","");
        // lấy data BanAn
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("BanAn");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listBA.clear();
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    BanAn ba = item.getValue(BanAn.class);
                    ba.setMaBanAn(item.getKey());
                    listBA.add(ba);
                }
                // sắp xếp theo tên - alphabet
                SapXepTheoTenBan(listBA);

                // cập nhật adapter
                adapterDisplayTable.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void SapXepTheoTenBan(ArrayList<BanAn> list){
        // sort by alphabet
        for(int i=0;i<list.size()-1;i++){
            for(int j=i+1;j<list.size();j++){
                if(list.get(i).getTenBanAn().length()>list.get(j).getTenBanAn().length()){
                    Collections.swap(list, i, j);
                }else if(list.get(i).getTenBanAn().length()==list.get(j).getTenBanAn().length()) {
                    if (list.get(i).getTenBanAn().compareTo(list.get(j).getTenBanAn()) > 0) {
                        Collections.swap(list, i, j);
                    }
                }
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        String maban = listBA.get(vitri).getMaBanAn();
        switch(id){
            case R.id.itEdit:
                Intent intent = new Intent(getActivity(), EditTableActivity.class);
                intent.putExtra("maban",maban);
                startActivity(intent);
                break;

            case R.id.itDelete:
                if(listBA.get(vitri).getTrangThai()==0||listBA.get(vitri).getMaDonDatHienTai().equals("null")){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("BanAn");
                    myRef.child(maban).removeValue();
                    Toast.makeText(getActivity(),"Xóa Bàn Thành Công", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Bàn Đang Có Khách", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // tạo menu trên tool bar
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itAddTable = menu.add(1,R.id.itAddTable,1,R.string.addTable);
        itAddTable.setIcon(R.drawable.ic_baseline_add_24);
        itAddTable.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.itAddTable:
                if(role.equals("QL")){
                    String maban = String.valueOf(UUID.randomUUID());
                    Intent iAddTable = new Intent(getActivity(), AddTableActivity.class);
                    iAddTable.putExtra("maban",maban);
                    startActivity(iAddTable);
                    break;
                }else{
                    Toast.makeText(getContext(),"Bạn không có quyền truy cập",Toast.LENGTH_SHORT).show();
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapterDisplayTable.notifyDataSetChanged();
    }

    private void HienThiDSBan(){
        adapterDisplayTable = new AdapterDisplayTable(getActivity(),R.layout.custom_layout_displaytable,listBA);
        GVDisplayTable.setAdapter(adapterDisplayTable);
        adapterDisplayTable.notifyDataSetChanged();
    }
}
