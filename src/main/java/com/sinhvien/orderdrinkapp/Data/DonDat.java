package com.sinhvien.orderdrinkapp.Data;

import java.util.UUID;

public class DonDat {
    String MaDonDat, MaBanAn, MaNV, NgayDat, HoVaTen, TenBan;
    int TinhTrang;
    long TongTien;

    public String getTenBan() {
        return TenBan;
    }

    public void setTenBan(String tenBan) {
        TenBan = tenBan;
    }

    public String getHoVaTen() {
        return HoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        HoVaTen = hoVaTen;
    }

    public String getMaDonDat() {
        return MaDonDat;
    }

    public void setMaDonDat(String maDonDat) {
        MaDonDat = maDonDat;
    }

    public String getMaBanAn() {
        return MaBanAn;
    }

    public void setMaBanAn(String maBanAn) {
        MaBanAn = maBanAn;
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String maNV) {
        MaNV = maNV;
    }

    public String getNgayDat() {
        return NgayDat;
    }

    public void setNgayDat(String ngayDat) {
        NgayDat = ngayDat;
    }

    public int getTinhTrang() {
        return TinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        TinhTrang = tinhTrang;
    }

    public long getTongTien() {
        return TongTien;
    }

    public void setTongTien(long tongTien) {
        TongTien = tongTien;
    }
}
