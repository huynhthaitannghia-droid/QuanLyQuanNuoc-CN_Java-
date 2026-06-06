package com.huit.QuanLyQuanCafe.repository;

import com.huit.QuanLyQuanCafe.entity.ChiTietHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon, Integer> {
}