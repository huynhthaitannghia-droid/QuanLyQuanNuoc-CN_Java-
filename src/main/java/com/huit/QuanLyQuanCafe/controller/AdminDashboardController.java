package com.huit.QuanLyQuanCafe.controller;

import com.huit.QuanLyQuanCafe.repository.BanRepository;
import com.huit.QuanLyQuanCafe.repository.ChiTietHoaDonRepository;
import com.huit.QuanLyQuanCafe.repository.HoaDonRepository;
import com.huit.QuanLyQuanCafe.repository.SanPhamRepository;
import com.huit.QuanLyQuanCafe.repository.NhanVienRepository;
import com.huit.QuanLyQuanCafe.repository.KetToanCaRepository;
import com.huit.QuanLyQuanCafe.repository.ToppingRepository;
import com.huit.QuanLyQuanCafe.entity.NhanVien;
import com.huit.QuanLyQuanCafe.entity.KetToanCa;
import com.huit.QuanLyQuanCafe.entity.HoaDon; // BỔ SUNG IMPORT NÀY

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// BỔ SUNG CÁC IMPORT THƯ VIỆN ĐỂ XỬ LÝ DỮ LIỆU
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

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
    private ToppingRepository toppingRepository;

    @GetMapping("/dashboard")
    public String hienThiTrangDashboard(Model model, HttpSession session) {
        // --- 1. XỬ LÝ 4 THẺ THỐNG KÊ TRÊN CÙNG ---
        Double doanhThuNay = hoaDonRepository.tinhDoanhThuHomNay();
        Double doanhThuQua = hoaDonRepository.tinhDoanhThuHomQua();
        doanhThuNay = (doanhThuNay != null) ? doanhThuNay : 0.0;
        doanhThuQua = (doanhThuQua != null) ? doanhThuQua : 0.0;

        String tenNV = (String) session.getAttribute("tenNV");
        model.addAttribute("tenAdmin", tenNV != null ? tenNV : "Admin");

        double tyLeTangTruong = 0.0;
        if (doanhThuQua > 0) {
            tyLeTangTruong = ((doanhThuNay - doanhThuQua) / doanhThuQua) * 100;
        } else if (doanhThuNay > 0) {
            tyLeTangTruong = 100.0;
        }

        model.addAttribute("doanhThuHomNay", doanhThuNay);
        model.addAttribute("tyLeTangTruong", Math.round(tyLeTangTruong * 10.0) / 10.0);

        Integer soDon = hoaDonRepository.demDonHangHomNay();
        model.addAttribute("donHangHomNay", soDon != null ? soDon : 0);

        Integer banPhucVu = banRepository.demBanDangPhucVu();
        model.addAttribute("banPhucVu", banPhucVu != null ? banPhucVu : 0);

        String monBanChay = chiTietHoaDonRepository.layTenSanPhamBanChayNhat();
        model.addAttribute("monBanChay", monBanChay != null ? monBanChay : "Chưa có dữ liệu");

        // --- ĐẾM SỐ BÀN VÀ TOPPING ---
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

        for (int i = 6; i >= 0; i--) {
            String dateStr = java.time.LocalDate.now().minusDays(i).format(formatter);
            chartLabels.add(dateStr);
            chartData.add(doanhThuMap.getOrDefault(dateStr, 0.0));
        }

        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", chartData);

        return "admin/dashboard";
    }

    // ==============================================================
    // API LẤY CHI TIẾT HÓA ĐƠN ĐỂ HIỂN THỊ POPUP
    // ==============================================================
    @GetMapping("/api/chi-tiet-hoa-don/{maHD}")
    @ResponseBody
    public ResponseEntity<?> getChiTietHoaDon(@PathVariable("maHD") Integer maHD) {
        List<Object[]> chiTiet = hoaDonRepository.layChiTietHoaDonTheoMa(maHD);
        return ResponseEntity.ok(chiTiet);
    }

    // ==============================================================
    // THÊM MỚI: API LẤY TOÀN BỘ LỊCH SỬ ĐƠN HÀNG (CÓ HÌNH THỨC TT)
    // ==============================================================
    @GetMapping("/api/lich-su-don-hang")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getLichSuDonHang() {
        List<HoaDon> hoaDons = hoaDonRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for(HoaDon hd : hoaDons) {
            Map<String, Object> map = new HashMap<>();
            map.put("maHD", hd.getMaHD());
            map.put("ngayTao", hd.getNgayTao() != null ? hd.getNgayTao().toString() : "");
            map.put("gioTao", hd.getGioTao() != null ? hd.getGioTao().toString() : "");
            map.put("tongTien", hd.getTongTien());
            map.put("hinhThuc", hd.getPhuongThucThanhToan());
            result.add(map);
        }
        return ResponseEntity.ok(result);
    }
}