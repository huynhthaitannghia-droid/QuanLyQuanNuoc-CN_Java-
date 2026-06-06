package com.huit.QuanLyQuanCafe.repository;

import com.huit.QuanLyQuanCafe.entity.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {
    // Tìm kiếm danh mục theo tên
    List<DanhMuc> findByTenDMContainingIgnoreCase(String keyword);
}