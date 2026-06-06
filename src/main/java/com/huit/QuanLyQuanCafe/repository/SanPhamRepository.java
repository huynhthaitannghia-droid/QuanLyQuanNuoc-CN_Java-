package com.huit.QuanLyQuanCafe.repository;

import com.huit.QuanLyQuanCafe.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    // Tìm theo Tên (TenSP), chứa từ khóa (Containing), không phân biệt hoa/thường (IgnoreCase)
    List<SanPham> findByTenSPContainingIgnoreCase(String keyword);
}