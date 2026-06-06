package com.huit.QuanLyQuanCafe.repository;

import com.huit.QuanLyQuanCafe.entity.Ban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface BanRepository extends JpaRepository<Ban, Integer> {
    // Đếm những bàn có trạng thái KHÁC chữ 'Trống'
    @Query("SELECT COUNT(b) FROM Ban b WHERE b.trangThai != 'Trống'")
    Integer demBanDangPhucVu();
}