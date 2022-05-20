package com.sinhvien.orderdrinkapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinhvien.orderdrinkapp.Data.ChiTietDonDat;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;

public class Test extends AppCompatActivity {
    ListView lvTest;
    ArrayAdapter adapter;
    ArrayList<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setControl();
        getData();
        setEvent();

    }

    private void setEvent() {
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,data);
        lvTest.setAdapter(adapter);
    }

    private void getData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ChiTietDonDat");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    ChiTietDonDat contact = item.getValue(ChiTietDonDat.class);
                    contact.setMaCTDD(item.getKey());
                    data.add(contact.getMaCTDD());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setControl() {
        lvTest = findViewById(R.id.lvTest);
    }
}