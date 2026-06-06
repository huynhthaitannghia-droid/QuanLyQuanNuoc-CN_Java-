package com.huit.QuanLyQuanCafe.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "NhanVien")
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNV")
    private Integer maNV;

    @Column(name = "TenNV", nullable = false, length = 100)
    private String tenNV;

    @Column(name = "SoDienThoai", length = 15)
    private String soDienThoai;

    @Column(name = "VaiTro", nullable = false, length = 20)
    private String vaiTro; // 'Admin' hoặc 'Staff'

    @Column(name = "TenDangNhap", nullable = false, unique = true, length = 50)
    private String tenDangNhap;

    @Column(name = "MatKhau", nullable = false, length = 255)
    private String matKhau;

    @Column(name = "TrangThai")
    private Integer trangThai; // 1: Đang làm, 0: Đã nghỉ
}