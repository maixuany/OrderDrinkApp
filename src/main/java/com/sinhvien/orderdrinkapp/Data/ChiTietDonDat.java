package com.sinhvien.orderdrinkapp.Data;

import java.util.UUID;

public class ChiTietDonDat {
    String MaCTDD,MaDonDat, MaMon, TenMon, HinhAnh;
    int SoLuong, TrangThai;
    Long TongTien;

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public String getTenMon() {
        return TenMon;
    }

    public void setTenMon(String tenMon) {
        TenMon = tenMon;
    }

    public Long getTongTien() {
        return TongTien;
    }

    public void setTongTien(Long tongTien) {
        TongTien = tongTien;
    }

    public String getMaDonDat() {
        return MaDonDat;
    }

    public void setMaDonDat(String maDonDat) {
        MaDonDat = maDonDat;
    }

    public String getMaCTDD() {
        return MaCTDD;
    }

    public void setMaCTDD(String maCTDD) {
        MaCTDD = maCTDD;
    }

    public String getMaMon() {
        return MaMon;
    }

    public void setMaMon(String maMon) {
        MaMon = maMon;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int soLuong) {
        SoLuong = soLuong;
    }

    public int getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(int trangThai) {
        TrangThai = trangThai;
    }

    @Override
    public String toString() {
        return "ChiTietDonDat{" +
                "MaCTDD='" + MaCTDD + '\'' +
                ", MaDonDat='" + MaDonDat + '\'' +
                ", MaMon='" + MaMon + '\'' +
                ", SoLuong=" + SoLuong +
                ", TrangThai=" + TrangThai +
                '}';
    }
}
