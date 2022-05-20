package com.sinhvien.orderdrinkapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinhvien.orderdrinkapp.Activities.AddStaffActivity;
import com.sinhvien.orderdrinkapp.Activities.HomeActivity;
import com.sinhvien.orderdrinkapp.CustomAdapter.AdapterDisplayStaff;
import com.sinhvien.orderdrinkapp.Data.NhanVien;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;

public class DisplayStaffFragment extends Fragment {

    ListView lvStaff;
    View view = null;
    ArrayList<NhanVien> listNhanVien = new ArrayList<>();
    AdapterDisplayStaff adapterDisplayStaff;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.displaystaff_layout,container,false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Quản lý nhân viên");
        setHasOptionsMenu(true);
        setControl();
        getData();
        setEvent();

        return view;
    }

    private void setEvent(){

    }

    private void getData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("NhanVien");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listNhanVien.clear();
                for(DataSnapshot item: snapshot.getChildren()){
                    NhanVien nhanVien = item.getValue(NhanVien.class);
                    nhanVien.setMaNV(item.getKey());
                    listNhanVien.add(nhanVien);
                }
                adapterDisplayStaff.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void setControl(){
        lvStaff = view.findViewById(R.id.lvStaff) ;
        HienThiDSNV();
        registerForContextMenu(lvStaff);
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
        String manv = listNhanVien.get(vitri).getMaNV();

        switch (id){
            case R.id.itEdit:
                Intent iEdit = new Intent(getActivity(),AddStaffActivity.class);
                iEdit.putExtra("manv",manv);
                startActivity(iEdit);
                break;

            case R.id.itDelete:
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("NhanVien");
                if(!listNhanVien.get(vitri).getMaQuyen().equals("QL")){
                    myRef.child(manv).removeValue();
                }else{
                    Toast.makeText(getContext(),"Không thể xóa tài khoản Quản Lý", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itAddStaff = menu.add(1,R.id.itAddStaff,1,"Thêm nhân viên");
        itAddStaff.setIcon(R.drawable.ic_baseline_add_24);
        itAddStaff.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itAddStaff:
                Intent iDangky = new Intent(getActivity(), AddStaffActivity.class);
                startActivity(iDangky);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void HienThiDSNV(){
        adapterDisplayStaff = new AdapterDisplayStaff(getActivity(),R.layout.custom_layout_displaystaff,listNhanVien);
        lvStaff.setAdapter(adapterDisplayStaff);
        adapterDisplayStaff.notifyDataSetChanged();
    }
}
