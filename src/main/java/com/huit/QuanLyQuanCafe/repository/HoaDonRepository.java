package com.huit.QuanLyQuanCafe.repository;

import com.huit.QuanLyQuanCafe.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {

    // Lấy 5 hóa đơn mới nhất
    List<HoaDon> findTop5ByOrderByMaHDDesc();

    // LẤY DOANH THU THẬT 7 NGÀY GẦN NHẤT ĐỂ VẼ BIỂU ĐỒ
    @Query(value = "SELECT DATE_FORMAT(NgayTao, '%d/%m') as Ngay, SUM(TongTien) as DoanhThu " +
            "FROM HoaDon " +
            "WHERE TrangThai = 1 AND NgayTao >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
            "GROUP BY NgayTao " +
            "ORDER BY NgayTao ASC", nativeQuery = true)
    List<Object[]> getDoanhThu7NgayGanNhat();

    // LẤY CHI TIẾT HÓA ĐƠN TRẢ VỀ MẢNG OBJECT (Tránh lỗi đệ quy JSON)
    @Query(value = "SELECT sp.TenSP, ct.SoLuong, ct.DonGia, ct.ThanhTien, ct.LuongDuong, ct.LuongDa, GROUP_CONCAT(t.TenTopping SEPARATOR ', ') as Toppings " +
            "FROM ChiTietHoaDon ct " +
            "JOIN SanPham sp ON ct.MaSP = sp.MaSP " +
            "LEFT JOIN ChiTietHoaDon_Topping ctt ON ct.MaCTHD = ctt.MaCTHD " +
            "LEFT JOIN Topping t ON ctt.MaTopping = t.MaTopping " +
            "WHERE ct.MaHD = ?1 " +
            "GROUP BY ct.MaCTHD, sp.TenSP, ct.SoLuong, ct.DonGia, ct.ThanhTien, ct.LuongDuong, ct.LuongDa",
            nativeQuery = true)
    List<Object[]> layChiTietHoaDonTheoMa(Integer maHD);

    // TÍNH TỔNG DOANH THU TRONG NGÀY HÔM NAY
    @Query(value = "SELECT SUM(TongTien) FROM HoaDon WHERE TrangThai = 1 AND NgayTao = CURDATE()", nativeQuery = true)
    Double tinhDoanhThuHomNay();

    // ĐẾM SỐ ĐƠN HÀNG BÁN ĐƯỢC HÔM NAY
    @Query(value = "SELECT COUNT(MaHD) FROM HoaDon WHERE NgayTao = CURDATE()", nativeQuery = true)
    Integer demDonHangHomNay();

    @Query(value = "SELECT SUM(TongTien) FROM HoaDon WHERE TrangThai = 1 AND NgayTao = DATE_SUB(CURDATE(), INTERVAL 1 DAY)", nativeQuery = true)
    Double tinhDoanhThuHomQua();
}