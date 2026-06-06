package com.huit.QuanLyQuanCafe.repository;

import com.huit.QuanLyQuanCafe.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    NhanVien findByTenDangNhap(String tenDangNhap);
}