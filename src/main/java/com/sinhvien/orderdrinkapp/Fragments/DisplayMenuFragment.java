package com.sinhvien.orderdrinkapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
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
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinhvien.orderdrinkapp.Activities.AddMenuActivity;
import com.sinhvien.orderdrinkapp.Activities.AmountMenuActivity;
import com.sinhvien.orderdrinkapp.Activities.HomeActivity;
import com.sinhvien.orderdrinkapp.CustomAdapter.AdapterDisplayMenu;
import com.sinhvien.orderdrinkapp.Data.Mon;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;
import java.util.UUID;

public class DisplayMenuFragment extends Fragment {

    String maloai;
    String tenloai;
    GridView gvDisplayMenu;
    View view = null;
    ArrayList<Mon> listMon = new ArrayList<>();
    AdapterDisplayMenu adapterDisplayMenu;
    String role = "";
    String maban, taobanmoi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.displaymenu_layout,container,false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Quản lý thực đơn");
        setControl();
        Bundle bundle = getArguments();
        if(bundle !=null){
            maloai = bundle.getString("maloai");
            tenloai = bundle.getString("tenloai");
            getData();
            setEvent();

        }
        setHasOptionsMenu(true);
        if(role.equals("QL")){
            if(maban.equals("")&&taobanmoi.equals("")){
                registerForContextMenu(gvDisplayMenu);
            }
        }
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                     getParentFragmentManager().popBackStack("hienthiloai", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                return false;
            }
        });

        return view;
    }

    private void getData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("session", getContext().MODE_PRIVATE);
        maban = sharedPreferences.getString("maban","");
        taobanmoi = sharedPreferences.getString("taobanmoi","");
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("session", getContext().MODE_PRIVATE);
        role = sharedPreferences.getString("role","");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Mon");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMon.clear();
                for(DataSnapshot item: snapshot.getChildren()){
                    Mon a = item.getValue(Mon.class);
                    a.setMaMon(item.getKey());
                    if(a.getMaLoaiMon().equals(maloai)) listMon.add(a);
                }
                adapterDisplayMenu.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setEvent() {

        System.out.println(maban+"/"+taobanmoi);
        HienThiDSMon();
        gvDisplayMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(maban.equals("")&&taobanmoi.equals("")){
                    return;
                }else{
                    int tinhtrangmon = listMon.get(position).getTinhTrang();
                    if(tinhtrangmon==0){
                        Toast.makeText(getContext(),"Món Đã Hết", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        Intent iAmount = new Intent(getActivity(), AmountMenuActivity.class);
                        iAmount.putExtra("mamon", listMon.get(position).getMaMon());
                        iAmount.putExtra("tenmon", listMon.get(position).getTenMon());
                        iAmount.putExtra("giatien", listMon.get(position).getGiaTien());
                        startActivity(iAmount);
                    }
                }
            }
        });
    }

    private void setControl() {
        gvDisplayMenu = (GridView)view.findViewById(R.id.gvDisplayMenu);
    }

    //tạo 1 menu context show lựa chọn
    @Override
    public void onCreateContextMenu(ContextMenu menu,View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu,menu);
    }

    //Tạo phần sửa và xóa trong menu context
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        Mon mon = listMon.get(vitri);
        switch (id){
            case R.id.itEdit:
                Intent iEdit = new Intent(getActivity(), AddMenuActivity.class);
                iEdit.putExtra("mamon",mon.getMaMon());
                iEdit.putExtra("maloai",maloai);
                iEdit.putExtra("tenloai",tenloai);
                iEdit.putExtra("tenmon",mon.getTenMon());
                iEdit.putExtra("giatien", mon.getGiaTien());
                iEdit.putExtra("ID","EDIT");
                iEdit.putExtra("trangthai",mon.getTinhTrang());
                startActivity(iEdit);
                break;

            case R.id.itDelete:
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Mon");
                myRef.child(mon.getMaMon()).removeValue();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itAddMenu = menu.add(1,R.id.itAddMenu,1,R.string.addMenu);
        itAddMenu.setIcon(R.drawable.ic_baseline_add_24);
        itAddMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itAddMenu:
                if(role.equals("QL")){
                    if(maban.equals("")&&taobanmoi.equals("")) {
                        String mamon = String.valueOf(UUID.randomUUID());
                        Intent intent = new Intent(getActivity(), AddMenuActivity.class);
                        intent.putExtra("maloai", maloai);
                        intent.putExtra("mamon", mamon);
                        intent.putExtra("tenloai", tenloai);
                        intent.putExtra("ID", "ADD");
                        startActivity(intent);
                        break;
                    }
                }else{
                    Toast.makeText(getContext(),"Bạn không có quyền truy cập",Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void HienThiDSMon(){
        adapterDisplayMenu = new AdapterDisplayMenu(getActivity(),R.layout.custom_layout_displaymenu,listMon);
        gvDisplayMenu.setAdapter(adapterDisplayMenu);
        adapterDisplayMenu.notifyDataSetChanged();
    }

}
