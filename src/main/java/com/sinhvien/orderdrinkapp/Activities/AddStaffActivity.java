package com.sinhvien.orderdrinkapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinhvien.orderdrinkapp.Data.NhanVien;
import com.sinhvien.orderdrinkapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Pattern;

public class AddStaffActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{6,}" +                // at least 4 characters
                    "$");

    ImageView IMG_addstaff_back;
    TextView TXT_addstaff_title;
    TextInputLayout TXTL_addstaff_HoVaTen, TXTL_addstaff_TenDN, TXTL_addstaff_Email, TXTL_addstaff_SDT, TXTL_addstaff_MatKhau;
    RadioGroup RG_addstaff_GioiTinh,rg_addstaff_Quyen;
    RadioButton RD_addstaff_Nam,RD_addstaff_Nu,RD_addstaff_Khac,rd_addstaff_PhaChe,rd_addstaff_NhanVien,rd_addstaff_ThuNgan;
    DatePicker DT_addstaff_NgaySinh;
    Button BTN_addstaff_ThemNV;
    ArrayList<NhanVien> listNhanVien = new ArrayList<>();
    String hoTen,tenDN,eMail,sDT,matKhau,gioiTinh,ngaySinh;
    String manv ,quyen, manv_temp ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addstaff_layout);
        setControl();
        getData();
        setEvent();
    }

    private void setEvent() {
        IMG_addstaff_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        BTN_addstaff_ThemNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !validateAge() | !validateEmail() | !validateFullName() | !validatePassWord() | !validatePhone() | !validateUserName()){
                    return;
                }
                hoTen = TXTL_addstaff_HoVaTen.getEditText().getText().toString();
                tenDN = TXTL_addstaff_TenDN.getEditText().getText().toString();
                eMail = TXTL_addstaff_Email.getEditText().getText().toString();
                sDT = TXTL_addstaff_SDT.getEditText().getText().toString();
                matKhau = TXTL_addstaff_MatKhau.getEditText().getText().toString();
                switch (RG_addstaff_GioiTinh.getCheckedRadioButtonId()){
                    case R.id.rd_addstaff_Nam: gioiTinh = "Nam"; break;
                    case R.id.rd_addstaff_Nu: gioiTinh = "Nữ"; break;
                    case R.id.rd_addstaff_Khac: gioiTinh = "Khác"; break;
                }
                switch (rg_addstaff_Quyen.getCheckedRadioButtonId()){
                    case R.id.rd_addstaff_PhaChe: quyen = "PC"; break;
                    case R.id.rd_addstaff_NhanVien: quyen = "NV"; break;
                    case R.id.rd_addstaff_ThuNgan: quyen = "TN"; break;
                    default:quyen="QL";
                }
                ngaySinh = DT_addstaff_NgaySinh.getDayOfMonth() + "-" + (DT_addstaff_NgaySinh.getMonth() + 1)
                        +"-"+DT_addstaff_NgaySinh.getYear();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("NhanVien");
                System.out.println("MA NHAN VIEN CREATE: "+manv_temp);
                myRef.child(manv_temp).child("Email").setValue(eMail);
                myRef.child(manv_temp).child("GioiTinh").setValue(gioiTinh);
                myRef.child(manv_temp).child("HoVaTen").setValue(hoTen);
                myRef.child(manv_temp).child("MaQuyen").setValue(quyen);
                myRef.child(manv_temp).child("MatKhau").setValue(matKhau);
                myRef.child(manv_temp).child("NgaySinh").setValue(ngaySinh);
                myRef.child(manv_temp).child("SDT").setValue(sDT);
                myRef.child(manv_temp).child("TenDN").setValue(tenDN);
                myRef.child(manv_temp).child("RecoveryCode").setValue("null");
                finish();

            }
        });


    }

    private void getData() {
        rd_addstaff_ThuNgan.setClickable(true);
        rd_addstaff_PhaChe.setClickable(true);
        rd_addstaff_NhanVien.setClickable(true);
        //region Hiển thị trang sửa nếu được chọn từ context menu sửa
        manv = getIntent().getStringExtra("manv");   //lấy manv từ display staff
        if(manv!=null){
            TXT_addstaff_title.setText("Sửa nhân viên");
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
                        if(nhanVien.getMaNV()!=null){
                            if(nhanVien.getMaNV().equals(manv)){
                                //Hiển thị thông tin từ csdl
                                TXTL_addstaff_HoVaTen.getEditText().setText(nhanVien.getHoVaTen());
                                TXTL_addstaff_TenDN.getEditText().setText(nhanVien.getTenDN());
                                TXTL_addstaff_Email.getEditText().setText(nhanVien.getEmail());
                                TXTL_addstaff_SDT.getEditText().setText(nhanVien.getSDT());
                                TXTL_addstaff_MatKhau.getEditText().setText(nhanVien.getMatKhau());
                                //Hiển thị giới tính từ csdl
                                String gioitinh = nhanVien.getGioiTinh();
                                if(gioitinh.equals("Nam")){
                                    RD_addstaff_Nam.setChecked(true);
                                }else if (gioitinh.equals("Nữ")){
                                    RD_addstaff_Nu.setChecked(true);
                                }else {
                                    RD_addstaff_Khac.setChecked(true);
                                }
                                if(nhanVien.getMaQuyen().equals("NV")){
                                    rd_addstaff_NhanVien.setChecked(true);
                                }else if(nhanVien.getMaQuyen().equals("PC")){
                                    rd_addstaff_PhaChe.setChecked(true);
                                }else if(nhanVien.getMaQuyen().equals("TN")){
                                    rd_addstaff_ThuNgan.setChecked(true);
                                }else{
                                    rd_addstaff_ThuNgan.setClickable(false);
                                    rd_addstaff_PhaChe.setClickable(false);
                                    rd_addstaff_NhanVien.setClickable(false);
                                }
                                //Hiển thị ngày sinh từ csdl
                                String date = nhanVien.getNgaySinh();
                                String[] items = date.split("-");
                                int day = Integer.parseInt(items[0]);
                                int month = Integer.parseInt(items[1]) - 1;
                                int year = Integer.parseInt(items[2]);
                                DT_addstaff_NgaySinh.updateDate(year,month,day);
                                BTN_addstaff_ThemNV.setText("Sửa nhân viên");
                                manv_temp = manv;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            manv_temp = String.valueOf(UUID.randomUUID());

            rd_addstaff_NhanVien.setChecked(true);
            RD_addstaff_Nam.setChecked(true);
        }
    }

    private void setControl() {
        //region Lấy đối tượng trong view
        TXT_addstaff_title = (TextView)findViewById(R.id.txt_addstaff_title);
        IMG_addstaff_back = (ImageView)findViewById(R.id.img_addstaff_back);
        TXTL_addstaff_HoVaTen = (TextInputLayout)findViewById(R.id.txtl_addstaff_HoVaTen);
        TXTL_addstaff_TenDN = (TextInputLayout)findViewById(R.id.txtl_addstaff_TenDN);
        TXTL_addstaff_Email = (TextInputLayout)findViewById(R.id.txtl_addstaff_Email);
        TXTL_addstaff_SDT = (TextInputLayout)findViewById(R.id.txtl_addstaff_SDT);
        TXTL_addstaff_MatKhau = (TextInputLayout)findViewById(R.id.txtl_addstaff_MatKhau);
        RG_addstaff_GioiTinh = (RadioGroup)findViewById(R.id.rg_addstaff_GioiTinh);
        rg_addstaff_Quyen = (RadioGroup)findViewById(R.id.rg_addstaff_Quyen);
        RD_addstaff_Nam = (RadioButton)findViewById(R.id.rd_addstaff_Nam);
        RD_addstaff_Nu = (RadioButton)findViewById(R.id.rd_addstaff_Nu);
        RD_addstaff_Khac = (RadioButton)findViewById(R.id.rd_addstaff_Khac);
        rd_addstaff_PhaChe = (RadioButton)findViewById(R.id.rd_addstaff_PhaChe);
        rd_addstaff_ThuNgan = (RadioButton)findViewById(R.id.rd_addstaff_ThuNgan);
        rd_addstaff_NhanVien = (RadioButton)findViewById(R.id.rd_addstaff_NhanVien);
        DT_addstaff_NgaySinh = (DatePicker)findViewById(R.id.dt_addstaff_NgaySinh);
        BTN_addstaff_ThemNV = (Button)findViewById(R.id.btn_addstaff_ThemNV);

        //endregion
    }

    //region validate fields
    private boolean validateFullName(){
        String val = TXTL_addstaff_HoVaTen.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            TXTL_addstaff_HoVaTen.setError(getResources().getString(R.string.not_empty));
            return false;
        }else {
            TXTL_addstaff_HoVaTen.setError(null);
            TXTL_addstaff_HoVaTen.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserName(){
        String val = TXTL_addstaff_TenDN.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,50}\\z";

        if(val.isEmpty()){
            TXTL_addstaff_TenDN.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(val.length()>50){
            TXTL_addstaff_TenDN.setError("Phải nhỏ hơn 50 ký tự");
            return false;
        }else if(!val.matches(checkspaces)){
            TXTL_addstaff_TenDN.setError("Không được cách chữ!");
            return false;
        }
        else {
            TXTL_addstaff_TenDN.setError(null);
            TXTL_addstaff_TenDN.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail(){
        String val = TXTL_addstaff_Email.getEditText().getText().toString().trim();
        String checkspaces = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if(val.isEmpty()){
            TXTL_addstaff_Email.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(!val.matches(checkspaces)){
            TXTL_addstaff_Email.setError("Email không hợp lệ!");
            return false;
        }
        else {
            TXTL_addstaff_Email.setError(null);
            TXTL_addstaff_Email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhone(){
        String val = TXTL_addstaff_SDT.getEditText().getText().toString().trim();


        if(val.isEmpty()){
            TXTL_addstaff_SDT.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(val.length() != 10){
            TXTL_addstaff_SDT.setError("Số điện thoại không hợp lệ!");
            return false;
        }
        else {
            TXTL_addstaff_SDT.setError(null);
            TXTL_addstaff_SDT.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassWord(){
        String val = TXTL_addstaff_MatKhau.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            TXTL_addstaff_MatKhau.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(!PASSWORD_PATTERN.matcher(val).matches()){
            TXTL_addstaff_MatKhau.setError("Mật khẩu ít nhất 6 ký tự!");
            return false;
        }
        else {
            TXTL_addstaff_MatKhau.setError(null);
            TXTL_addstaff_MatKhau.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateAge(){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = DT_addstaff_NgaySinh.getYear();
        int isAgeValid = currentYear - userAge;

        if(isAgeValid < 10){
            Toast.makeText(this,"Bạn không đủ tuổi đăng ký!",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
    //endregion

}