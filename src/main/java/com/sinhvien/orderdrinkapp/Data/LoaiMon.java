package com.sinhvien.orderdrinkapp.Data;

import java.io.Serializable;
import java.util.UUID;

public class LoaiMon implements Serializable {
    String MaLoaiMon, TenLoai;
    String HinhAnh;

    public String getMaLoaiMon() {
        return MaLoaiMon;
    }

    public void setMaLoaiMon(String maLoaiMon) {
        MaLoaiMon = maLoaiMon;
    }

    public String getTenLoai() {
        return TenLoai;
    }

    public void setTenLoai(String tenLoai) {
        TenLoai = tenLoai;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }

    @Override
    public String toString() {
        return "LoaiMon{" +
                "MaLoaiMon='" + MaLoaiMon + '\'' +
                ", TenLoai='" + TenLoai + '\'' +
                ", HinhAnh='" + HinhAnh + '\'' +
                '}';
    }
}
