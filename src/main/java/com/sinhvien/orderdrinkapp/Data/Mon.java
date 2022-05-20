package com.sinhvien.orderdrinkapp.Data;

import java.util.UUID;

public class Mon {
    String MaMon, MaLoaiMon, TenMon;
    int TinhTrang;
    String HinhAnh;
    long GiaTien;

    public long getGiaTien() {
        return GiaTien;
    }

    public void setGiaTien(long giaTien) {
        GiaTien = giaTien;
    }

    public String getMaMon() {
        return MaMon;
    }

    public void setMaMon(String maMon) {
        MaMon = maMon;
    }

    public String getMaLoaiMon() {
        return MaLoaiMon;
    }

    public void setMaLoaiMon(String maLoaiMon) {
        MaLoaiMon = maLoaiMon;
    }

    public String getTenMon() {
        return TenMon;
    }

    public void setTenMon(String tenMon) {
        TenMon = tenMon;
    }

    public int getTinhTrang() {
        return TinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        TinhTrang = tinhTrang;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }
}
