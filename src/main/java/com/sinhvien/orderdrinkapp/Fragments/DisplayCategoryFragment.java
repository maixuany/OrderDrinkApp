package com.sinhvien.orderdrinkapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinhvien.orderdrinkapp.Activities.AddCategoryActivity;
import com.sinhvien.orderdrinkapp.Activities.HomeActivity;
import com.sinhvien.orderdrinkapp.CustomAdapter.AdapterDisplayCategory;
import com.sinhvien.orderdrinkapp.Data.LoaiMon;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;

public class DisplayCategoryFragment extends Fragment {

    GridView gvCategory;
    AdapterDisplayCategory adapter;
    FragmentManager fragmentManager;
    View view = null;
    ArrayList<LoaiMon> listLM = new ArrayList<>();
    String role, maban, taobanmoi;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.displaycategory_layout,container,false);
        setHasOptionsMenu(true);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Quản lý thực đơn");
        setControl();
        getData();
        fragmentManager = getActivity().getSupportFragmentManager();
        HienThiDSLoai();
        setEvent();
        if(role.equals("QL")){
            if(maban.equals("")&&taobanmoi.equals("")) {
                registerForContextMenu(gvCategory);
            }
        }

        return view;
    }

    private void setControl(){
        gvCategory = (GridView)view.findViewById(R.id.gvCategory);

    }

    private void setEvent() {
        gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String maloai = listLM.get(position).getMaLoaiMon();
                DisplayMenuFragment displayMenuFragment = new DisplayMenuFragment();
                Bundle bundle = new Bundle();
                bundle.putString("maloai",maloai);
                bundle.putString("tenloai", listLM.get(position).getTenLoai());
//                bundle.putInt("maban",maban);
                displayMenuFragment.setArguments(bundle);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.contentView,displayMenuFragment).addToBackStack("hienthiloai");
                transaction.commit();
            }
        });
    }

    private void getData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("session", getActivity().MODE_PRIVATE);
        role = sharedPreferences.getString("role","");
        taobanmoi = sharedPreferences.getString("taobanmoi","");
        maban = sharedPreferences.getString("maban","");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("LoaiMon");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listLM.clear();
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    LoaiMon a = item.getValue(LoaiMon.class);
                    a.setMaLoaiMon(item.getKey());
                    listLM.add(a);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //hiển thị contextmenu
    @Override
    public void onCreateContextMenu(ContextMenu menu,View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu,menu);
    }

    //xử lí context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        LoaiMon loaiMon = listLM.get(vitri);
        switch (id){
            case R.id.itEdit:
                Intent iEdit = new Intent(getActivity(), AddCategoryActivity.class);
                iEdit.putExtra("maLoaiMon", loaiMon.getMaLoaiMon());
                iEdit.putExtra("tenLoai", loaiMon.getTenLoai());
                iEdit.putExtra("ID","EDIT");
                startActivity(iEdit);
                break;

            case R.id.itDelete:
                Toast.makeText(getActivity(), "Đây là các Menu cố định",Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    //khởi tạo nút thêm loại
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itAddCategory = menu.add(1,R.id.itAddCategory,1,R.string.addCategory);
        itAddCategory.setIcon(R.drawable.ic_baseline_add_24);
        itAddCategory.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    //xử lý nút thêm loại
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itAddCategory:
                if(role.equals("QL")){
                    if(maban.equals("")&&taobanmoi.equals("")) {
                        Intent intent = new Intent(getActivity(), AddCategoryActivity.class);
                        intent.putExtra("ID", "ADD");
                        startActivity(intent);
                        break;
                    }
                }else{
                    Toast.makeText(getActivity(),"Bạn không có quyền truy cập",Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void HienThiDSLoai(){
        adapter = new AdapterDisplayCategory(getActivity(),R.layout.custom_layout_displaycategory,listLM);
        gvCategory.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
