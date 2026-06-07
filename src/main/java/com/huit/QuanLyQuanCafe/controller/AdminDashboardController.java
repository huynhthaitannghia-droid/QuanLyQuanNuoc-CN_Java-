package com.huit.QuanLyQuanCafe.controller;

import com.huit.QuanLyQuanCafe.repository.BanRepository;
import com.huit.QuanLyQuanCafe.repository.ChiTietHoaDonRepository;
import com.huit.QuanLyQuanCafe.repository.HoaDonRepository;
import com.huit.QuanLyQuanCafe.repository.SanPhamRepository;
import com.huit.QuanLyQuanCafe.repository.NhanVienRepository;
import com.huit.QuanLyQuanCafe.repository.KetToanCaRepository;
import com.huit.QuanLyQuanCafe.repository.ToppingRepository; // THÊM IMPORT NÀY
import com.huit.QuanLyQuanCafe.entity.NhanVien;
import com.huit.QuanLyQuanCafe.entity.KetToanCa;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private BanRepository banRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private KetToanCaRepository ketToanCaRepository;

    @Autowired
    private ToppingRepository toppingRepository; // THÊM REPOSITORY NÀY

    @GetMapping("/dashboard")
    public String hienThiTrangDashboard(Model model, HttpSession session) {
        // --- 1. XỬ LÝ 4 THẺ THỐNG KÊ TRÊN CÙNG ---
        Double doanhThuNay = hoaDonRepository.tinhDoanhThuHomNay();
        Double doanhThuQua = hoaDonRepository.tinhDoanhThuHomQua();
        doanhThuNay = (doanhThuNay != null) ? doanhThuNay : 0.0;
        doanhThuQua = (doanhThuQua != null) ? doanhThuQua : 0.0;

        // Lấy tên nhân viên từ session (đã lưu lúc đăng nhập)
        String tenNV = (String) session.getAttribute("tenNV");
        model.addAttribute("tenAdmin", tenNV != null ? tenNV : "Admin");

        // Tính tỷ lệ % tăng/giảm doanh thu
        double tyLeTangTruong = 0.0;
        if (doanhThuQua > 0) {
            tyLeTangTruong = ((doanhThuNay - doanhThuQua) / doanhThuQua) * 100;
        } else if (doanhThuNay > 0) {
            tyLeTangTruong = 100.0; // Nếu hôm qua 0đ mà hôm nay có tiền thì auto tăng 100%
        }

        model.addAttribute("doanhThuHomNay", doanhThuNay);
        model.addAttribute("tyLeTangTruong", Math.round(tyLeTangTruong * 10.0) / 10.0); // Làm tròn 1 chữ số thập phân

        Integer soDon = hoaDonRepository.demDonHangHomNay();
        model.addAttribute("donHangHomNay", soDon != null ? soDon : 0);

        Integer banPhucVu = banRepository.demBanDangPhucVu();
        model.addAttribute("banPhucVu", banPhucVu != null ? banPhucVu : 0);

        String monBanChay = chiTietHoaDonRepository.layTenSanPhamBanChayNhat();
        model.addAttribute("monBanChay", monBanChay != null ? monBanChay : "Chưa có dữ liệu");

        // --- BỔ SUNG: ĐẾM SỐ BÀN VÀ TOPPING ---
        long tongSoBan = banRepository.count();
        model.addAttribute("tongSoBan", tongSoBan);

        long tongSoTopping = toppingRepository.count();
        model.addAttribute("tongSoTopping", tongSoTopping);

        // --- 2. LẤY 5 ĐƠN HÀNG MỚI NHẤT ---
        model.addAttribute("donHangMoi", hoaDonRepository.findTop5ByOrderByMaHDDesc());

        // --- 3. THUẬT TOÁN ĐỒNG BỘ 7 NGÀY CHO BIỂU ĐỒ ---
        List<Object[]> doanhThuDB = hoaDonRepository.getDoanhThu7NgayGanNhat();
        java.util.Map<String, Double> doanhThuMap = new java.util.HashMap<>();
        for (Object[] row : doanhThuDB) {
            doanhThuMap.put(row[0].toString(), Double.parseDouble(row[1].toString()));
        }

        List<String> chartLabels = new java.util.ArrayList<>();
        List<Double> chartData = new java.util.ArrayList<>();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM");

        // Chạy vòng lặp lùi từ 6 ngày trước đến hôm nay (Tổng cộng 7 ngày)
        for (int i = 6; i >= 0; i--) {
            String dateStr = java.time.LocalDate.now().minusDays(i).format(formatter);
            chartLabels.add(dateStr); // Cột mốc ngày (VD: 01/06)
            chartData.add(doanhThuMap.getOrDefault(dateStr, 0.0)); // Nếu không có tiền -> Gán 0.0
        }

        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", chartData);

        return "admin/dashboard";
    }

    // API LẤY CHI TIẾT HÓA ĐƠN ĐỂ HIỂN THỊ POPUP
    @GetMapping("/api/chi-tiet-hoa-don/{maHD}")
    @ResponseBody
    public ResponseEntity<?> getChiTietHoaDon(@PathVariable("maHD") Integer maHD) {
        List<Object[]> chiTiet = hoaDonRepository.layChiTietHoaDonTheoMa(maHD);
        return ResponseEntity.ok(chiTiet);
    }
}