package com.sinhvien.orderdrinkapp.Data;

import java.util.UUID;

public class BanAn {
    String MaBanAn, TenBanAn;
    int TrangThai;
    String MaDonDatHienTai;

    public String getMaBanAn() {
        return MaBanAn;
    }

    public void setMaBanAn(String maBanAn) {
        MaBanAn = maBanAn;
    }

    public String getTenBanAn() {
        return TenBanAn;
    }

    public void setTenBanAn(String tenBanAn) {
        TenBanAn = tenBanAn;
    }

    public int getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(int trangThai) {
        TrangThai = trangThai;
    }

    public String getMaDonDatHienTai() {
        return MaDonDatHienTai;
    }

    public void setMaDonDatHienTai(String maDonDatHienTai) {
        MaDonDatHienTai = maDonDatHienTai;
    }
}
