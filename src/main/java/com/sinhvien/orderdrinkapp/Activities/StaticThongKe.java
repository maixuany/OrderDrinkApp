package com.sinhvien.orderdrinkapp.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinhvien.orderdrinkapp.Data.DonDat;
import com.sinhvien.orderdrinkapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

@SuppressLint("SimpleDateFormat")
public class StaticThongKe extends AppCompatActivity {

    ArrayList<DonDat> listAllDonDat =  new ArrayList<>();
    ArrayList<DonDat> listDonDatDaThanhToan =  new ArrayList<>();

    BarChart barChart;
    ArrayList<String> dates;
    Random random;
    ArrayList<BarEntry> barEntries;
    TextView tvbatdau,tvketthuc;
    FloatingActionButton floatingActionButton;

    final Calendar myCalendar1= Calendar.getInstance();
    final Calendar myCalendar2= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_thong_ke);

        setControl();
        setEvent();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date date = new Date();
        // date now
        tvketthuc.setText(simpleDateFormat.format(date));
        // date now - 7 day
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        tvbatdau.setText(simpleDateFormat.format(cal.getTime()));
        createRandomBarGraph(tvbatdau.getText().toString(), tvketthuc.getText().toString());

        // x??t ch???n ng??y b???t ?????u
        DatePickerDialog.OnDateSetListener dates =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH,month);
                myCalendar1.set(Calendar.DAY_OF_MONTH,day);
                String myFormat="dd-MM-yyyy";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                tvbatdau.setText(dateFormat.format(myCalendar1.getTime()));
            }
        };

        // hi???n th??? calendar ????? pick th???i gian
        tvbatdau.setOnClickListener(view ->{
            new DatePickerDialog(StaticThongKe.this,dates,myCalendar1.get(Calendar.YEAR),myCalendar1.get(Calendar.MONTH),myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
        });

        // x??t ch???n ng??y k???t th??c
        DatePickerDialog.OnDateSetListener date2 =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH,month);
                myCalendar2.set(Calendar.DAY_OF_MONTH,day);
                String myFormat="dd-MM-yyyy";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                tvketthuc.setText(dateFormat.format(myCalendar2.getTime()));
            }
        };

        // hi???n th??? calendar ????? pick th???i gian
        tvketthuc.setOnClickListener(view ->{
            new DatePickerDialog(StaticThongKe.this,date2,myCalendar2.get(Calendar.YEAR),myCalendar2.get(Calendar.MONTH),myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
        });

        floatingActionButton.setOnClickListener(view->{
            // n???u ng??y b???t ?????u < ng??y k???t th??c
            if(myCalendar1.before(myCalendar2)){
                createRandomBarGraph(tvbatdau.getText().toString(), tvketthuc.getText().toString());
                barChart.notifyDataSetChanged();
                Toast.makeText(StaticThongKe.this,"Ch???m v??o bi???u ????? ????? RESET",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(StaticThongKe.this,"D??? li???u ????a v??o kh??ng h???p l???",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setEvent() {
        getData();
    }

    private void getData() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("DonDat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listAllDonDat.clear();
                listDonDatDaThanhToan.clear();
                for (DataSnapshot item: snapshot.getChildren()){
                    DonDat donDat = item.getValue(DonDat.class);
                    donDat.setMaDonDat(item.getKey());
                    listAllDonDat.add(donDat);
                    if(donDat.getTinhTrang()==1){
                        listDonDatDaThanhToan.add(donDat);
                    }
                }
                Log.i("thongke","All don : "+ listAllDonDat.size());
                Log.i("thongke","Don thanh toan : "+ listDonDatDaThanhToan.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        barChart = (BarChart) findViewById(R.id.barchart);
        tvbatdau = findViewById(R.id.tvbatdau);
        tvketthuc = findViewById(R.id.tvketthuc);
        floatingActionButton = findViewById(R.id.floatingActionButton);
    }


    // t???o tr???c graph
    public void createRandomBarGraph(String Date1, String Date2){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date date1 = simpleDateFormat.parse(Date1);
            Date date2 = simpleDateFormat.parse(Date2);

            Calendar mDate1 = Calendar.getInstance();
            Calendar mDate2 = Calendar.getInstance();
            mDate1.clear();
            mDate2.clear();

            mDate1.setTime(date1);
            mDate2.setTime(date2);

            dates = new ArrayList<>();
            dates = getList(mDate1,mDate2);

            barEntries = new ArrayList<>();
            random = new Random();
            for(int j = 0; j< dates.size();j++){
                long tong = 0;
                for(DonDat dt : listDonDatDaThanhToan){
                    if(dt.getNgayDat().equals(dates.get(j))){
                        tong+= ((long) dt.getTongTien());
                    }
                }
                // x = t???ng ti???n ng??y ????: tong
                //y = ng??y ????           :j - v??? tr?? dates
                barEntries.add(new BarEntry(tong,j));
            }

        }catch(ParseException e){
            e.printStackTrace();
        }
        // Bar Chart s??? d???ng 2 list ????? bi???u di???n tr???c x, tr???c y
        // tr???c x: x??t gi?? tr??? list th???i gian
        // tr???c y: x??t c??c BarDataSet l?? t???p h???p c??c (barEntry, ????n v??? t??nh)
        // BarEntry l?? gi?? tr??? t???ng ti???n ng??y ????, v???i v??? tr?? l?? v??? tr?? dates trong kho???ng th???i gian x??t
        BarDataSet barDataSet = new BarDataSet(barEntries,"Vietnam ?????ng");
        BarData barData = new BarData(dates,barDataSet);
        barChart.setData(barData);

    }


    // l???y danh s??ch t???ng ng??y trong 1 kho???ng ng??y
    public ArrayList<String> getList(Calendar startDate, Calendar endDate){
        ArrayList<String> list = new ArrayList<String>();
        while(startDate.compareTo(endDate)<=0){
            list.add(getDate(startDate));
            startDate.add(Calendar.DAY_OF_MONTH,1);
        }
        return list;
    }

    public String getDate(Calendar cld){
        String curDate = cld.get(Calendar.DAY_OF_MONTH) + "-" + (cld.get(Calendar.MONTH) + 1)
                + "-"+ cld.get(Calendar.YEAR);
        try{
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(curDate);
            curDate =  new SimpleDateFormat("dd-MM-yyyy").format(date);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return curDate;
    }

}
