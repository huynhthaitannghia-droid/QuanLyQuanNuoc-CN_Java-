package com.huit.QuanLyQuanCafe.repository;

import com.huit.QuanLyQuanCafe.entity.ChiTietHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon, Integer> {

    // Di chuyển hàm này từ HoaDonRepository sang đây
    @Query(value = "SELECT sp.TenSP, ct.SoLuong, ct.DonGia, ct.ThanhTien, ct.LuongDuong, ct.LuongDa " +
            "FROM ChiTietHoaDon ct " +
            "JOIN SanPham sp ON ct.MaSP = sp.MaSP " +
            "WHERE ct.MaHD = :maHD", nativeQuery = true)
    List<Object[]> layChiTietHoaDonTheoMa(Integer maHD);

    // Thêm hàm lấy món bán chạy nhất vào đây luôn nhé
    @Query(value = "SELECT sp.TenSP FROM ChiTietHoaDon ct JOIN SanPham sp ON ct.MaSP = sp.MaSP GROUP BY ct.MaSP, sp.TenSP ORDER BY SUM(ct.SoLuong) DESC LIMIT 1", nativeQuery = true)
    String layTenSanPhamBanChayNhat();
}