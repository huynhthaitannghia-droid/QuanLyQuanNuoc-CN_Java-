package com.huit.QuanLyQuanCafe.repository;

import com.huit.QuanLyQuanCafe.entity.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {
    // Để trống thế này thôi, không cần gõ thêm bất kỳ dòng code nào nữa!
}