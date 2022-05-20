package com.sinhvien.orderdrinkapp.Data;

import java.util.UUID;

public class NhanVien {
    String MaNV, TenDN, MatKhau, MaQuyen, NgaySinh, SDT, GioiTinh, HoVaTen, Email, RecoveryCode;

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String maNV) {
        MaNV = maNV;
    }

    public String getTenDN() {
        return TenDN;
    }

    public void setTenDN(String tenDN) {
        TenDN = tenDN;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String matKhau) {
        MatKhau = matKhau;
    }

    public String getMaQuyen() {
        return MaQuyen;
    }

    public void setMaQuyen(String maQuyen) {
        MaQuyen = maQuyen;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        NgaySinh = ngaySinh;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        GioiTinh = gioiTinh;
    }

    public String getHoVaTen() {
        return HoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        HoVaTen = hoVaTen;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getRecoveryCode() {
        return RecoveryCode;
    }

    public void setRecoveryCode(String recoveryCode) {
        RecoveryCode = recoveryCode;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "MaNV='" + MaNV + '\'' +
                ", TenDN='" + TenDN + '\'' +
                ", MatKhau='" + MatKhau + '\'' +
                ", MaQuyen='" + MaQuyen + '\'' +
                ", NgaySinh='" + NgaySinh + '\'' +
                ", SDT='" + SDT + '\'' +
                ", GioiTinh='" + GioiTinh + '\'' +
                ", HoVaTen='" + HoVaTen + '\'' +
                '}';
    }
}
