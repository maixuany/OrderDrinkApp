package com.sinhvien.orderdrinkapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinhvien.orderdrinkapp.Fragments.DisplayCategoryFragment;
import com.sinhvien.orderdrinkapp.Fragments.DisplayHomeFragment;
import com.sinhvien.orderdrinkapp.Fragments.DisplayStaffFragment;
import com.sinhvien.orderdrinkapp.Fragments.DisplayStatisticFragment;
import com.sinhvien.orderdrinkapp.Fragments.DisplayTableFragment;
import com.sinhvien.orderdrinkapp.R;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FragmentManager fragmentManager;
    TextView TXT_menu_tennv;
    String role = "";
    String hoten = "";
    Intent intent;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        setControl();
        setEvent();

        //lấy file share prefer
        sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");
        hoten = sharedPreferences.getString("tenNV", "");
        TXT_menu_tennv.setText("Xin chào " + hoten + " !!");

        //hiện thị fragment home mặc định
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction tranDisplayHome = fragmentManager.beginTransaction();
        DisplayHomeFragment displayHomeFragment = new DisplayHomeFragment();
        tranDisplayHome.replace(R.id.contentView, displayHomeFragment);
        tranDisplayHome.commit();
        navigationView.setCheckedItem(R.id.nav_home);
    }

    private void setEvent() {
    }

    private void setControl() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view_trangchu);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        View view = navigationView.getHeaderView(0);
        TXT_menu_tennv = (TextView) view.findViewById(R.id.txt_menu_tennv);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar
                , R.string.opentoggle, R.string.closetoggle) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                //hiển thị tương ứng trên navigation
                FragmentTransaction tranDisplayHome = fragmentManager.beginTransaction();
                DisplayHomeFragment displayHomeFragment = new DisplayHomeFragment();
                tranDisplayHome.replace(R.id.contentView, displayHomeFragment);
//                tranDisplayHome.addToBackStack(null);
                tranDisplayHome.commit();
                navigationView.setCheckedItem(item.getItemId());
                drawerLayout.closeDrawers();
                break;

            case R.id.nav_statistic:
                //hiển thị tương ứng trên navigation
                FragmentTransaction tranDisplayStatistic = fragmentManager.beginTransaction();
                DisplayStatisticFragment displayStatisticFragment = new DisplayStatisticFragment();
                tranDisplayStatistic.replace(R.id.contentView, displayStatisticFragment);
                tranDisplayStatistic.addToBackStack(null);
                tranDisplayStatistic.commit();
                navigationView.setCheckedItem(item.getItemId());
                drawerLayout.closeDrawers();
                break;

            case R.id.nav_table:
                //hiển thị tương ứng trên navigation
                FragmentTransaction tranDisplayTable = fragmentManager.beginTransaction();
                DisplayTableFragment displayTableFragment = new DisplayTableFragment();
                tranDisplayTable.replace(R.id.contentView, displayTableFragment);
                tranDisplayTable.addToBackStack(null);
                tranDisplayTable.commit();
                navigationView.setCheckedItem(item.getItemId());
                drawerLayout.closeDrawers();
                break;

            case R.id.nav_category:
                //hiển thị tương ứng trên navigation
                sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                editor1.putString("maban", null);
                editor1.putString("taobanmoi", null);
                editor1.apply();
                FragmentTransaction tranDisplayMenu = fragmentManager.beginTransaction();
                DisplayCategoryFragment displayCategoryFragment = new DisplayCategoryFragment();
                tranDisplayMenu.replace(R.id.contentView, displayCategoryFragment);
                tranDisplayMenu.addToBackStack(null);
                tranDisplayMenu.commit();
                navigationView.setCheckedItem(item.getItemId());
                drawerLayout.closeDrawers();

                break;

            case R.id.nav_staff:
                if (role.equals("QL")) {
                    //hiển thị tương ứng trên navigation
                    FragmentTransaction tranDisplayStaff = fragmentManager.beginTransaction();
                    DisplayStaffFragment displayStaffFragment = new DisplayStaffFragment();
                    tranDisplayStaff.replace(R.id.contentView, displayStaffFragment);
                    tranDisplayStaff.addToBackStack(null);
                    tranDisplayStaff.commit();
                    navigationView.setCheckedItem(item.getItemId());
                    drawerLayout.closeDrawers();
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn không có quyền truy cập", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.nav_logout:
                sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("remember", "false");
                editor.putString("username", "");
                editor.putString("role", "");
                editor.apply();
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}