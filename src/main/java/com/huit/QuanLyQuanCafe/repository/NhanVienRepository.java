package com.huit.QuanLyQuanCafe.repository;

import com.huit.QuanLyQuanCafe.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> { // Lưu ý: Chữ String ở đây đại diện cho kiểu dữ liệu của ID, nếu ID của bạn là Integer thì đổi lại nhé
    // Tìm kiếm nhân viên theo tên
    List<NhanVien> findByTenNVContainingIgnoreCase(String keyword);

    NhanVien findByTenDangNhap(String tenDangNhap);

    List<NhanVien> findByTrangThai(Integer trangThai);
}