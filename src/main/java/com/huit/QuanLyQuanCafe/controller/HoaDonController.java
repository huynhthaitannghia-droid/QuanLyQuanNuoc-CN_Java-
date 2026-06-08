package com.huit.QuanLyQuanCafe.controller;

import com.huit.QuanLyQuanCafe.dto.OrderRequest;
import com.huit.QuanLyQuanCafe.entity.*;
import com.huit.QuanLyQuanCafe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/hoa-don")
public class HoaDonController {

    @Autowired
    private KetToanCaRepository ketToanCaRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private BanRepository banRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private ToppingRepository toppingRepository;

    @GetMapping
    public String hienThiDanhSachHoaDon(Model model) {
        model.addAttribute("listHD", hoaDonRepository.findAll());
        return "hoadon/index";
    }

    @GetMapping("/ban-hang")
    public String hienThiManHinhBanHang(Model model) {
        model.addAttribute("listBan", banRepository.findAll());
        model.addAttribute("listSP", sanPhamRepository.findAll());
        return "hoadon/ban-hang";
    }

    @GetMapping("/order/{maBan}")
    public String hienThiManHinhOrder(@PathVariable("maBan") Integer maBan, Model model) {
        Ban banDuocChon = banRepository.findById(maBan).orElse(null);
        model.addAttribute("ban", banDuocChon);
        model.addAttribute("listDM", danhMucRepository.findAll());
        model.addAttribute("listSP", sanPhamRepository.findAll());
        return "hoadon/order";
    }

    @GetMapping("/thanh-toan/{maBan}")
    public String hienThiManHinhThanhToan(@PathVariable("maBan") Integer maBan, Model model) {
        model.addAttribute("ban", banRepository.findById(maBan).orElse(null));
        return "hoadon/thanh-toan";
    }

    // --- API LƯU HÓA ĐƠN VÀO DATABASE ---
    @PostMapping("/luu-thanh-toan")
    @ResponseBody
    public String luuThanhToanDatabase(@RequestBody OrderRequest request, HttpSession session) {
        try {
            Integer maNV = (Integer) session.getAttribute("maNV");
            if (maNV == null) {
                return "LỖI: Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại!";
            }

            // 1. TẠO HÓA ĐƠN MỚI
            HoaDon hd = new HoaDon();
            hd.setTongTien(request.getTongTien());
            hd.setPhuongThucThanhToan(request.getPhuongThuc());
            hd.setLoaiDon("Tại quán");
            hd.setTrangThai(1);

            hd.setNgayTao(java.time.LocalDate.now());
            hd.setGioTao(java.time.LocalTime.now());

            Ban ban = banRepository.findById(request.getMaBan()).orElse(null);
            hd.setBan(ban);

            NhanVien nv = nhanVienRepository.findById(maNV)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên trong Database"));
            hd.setNhanVien(nv);

            HoaDon savedHD = hoaDonRepository.save(hd);

            // 2. LƯU TỪNG MÓN VÀO CHI TIẾT HÓA ĐƠN
            for (OrderRequest.CartItem item : request.getItems()) {
                ChiTietHoaDon ct = new ChiTietHoaDon();
                ct.setHoaDon(savedHD);

                SanPham sp = sanPhamRepository.findById(item.getId()).orElse(null);
                ct.setSanPham(sp);

                ct.setSoLuong(item.getSoLuong());
                ct.setDonGia(item.getGia());
                ct.setThanhTien(item.getGia().multiply(new BigDecimal(item.getSoLuong())));

                // Cập nhật Lượng Đường & Đá lấy từ DTO (nếu null thì dùng mặc định)
                ct.setLuongDuong(item.getLuongDuong() != null ? item.getLuongDuong() : "100% đường");
                ct.setLuongDa(item.getLuongDa() != null ? item.getLuongDa() : "Đá chung");

                // Xử lý lưu Topping (Ánh xạ ManyToMany)
                if (item.getToppingIds() != null && !item.getToppingIds().isEmpty()) {
                    List<Topping> toppings = toppingRepository.findAllById(item.getToppingIds());
                    ct.setListToppings(toppings);
                    // Lưu ý: Trong file Entity ChiTietHoaDon.java phải có List<Topping> toppings kèm annotation @ManyToMany nhé.
                }

                chiTietHoaDonRepository.save(ct);
            }

            // 3. ĐỔI TRẠNG THÁI BÀN VỀ TRỐNG
            if (ban != null) {
                ban.setTrangThai("Trống");
                banRepository.save(ban);
            }

            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return "LỖI: " + e.getMessage();
        }
    }

    // --- API CẬP NHẬT TRẠNG THÁI BÀN (LƯU TẠM) ---
    @PostMapping("/cap-nhat-trang-thai-ban")
    @ResponseBody
    public String capNhatTrangThaiBan(@RequestParam("maBan") Integer maBan, @RequestParam("trangThai") String trangThai) {
        try {
            Ban ban = banRepository.findById(maBan).orElse(null);
            if (ban != null) {
                ban.setTrangThai(trangThai);
                banRepository.save(ban);
                return "OK";
            }
            return "Không tìm thấy bàn";
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi: " + e.getMessage();
        }
    }

    // --- API LƯU TỔNG KẾT DOANH THU CA (ĐÃ ĐƯỢC NÂNG CẤP) ---
    @PostMapping("/luu-dong-ca")
    @ResponseBody
    public String luuDongCa(@RequestParam("tongTien") Double tongTien, HttpSession session) {
        try {
            Integer maNV = (Integer) session.getAttribute("maNV");

            if (maNV == null) {
                return "Lỗi: Phiên làm việc đã hết hạn. Không tìm thấy thông tin nhân viên!";
            }

            // Tìm ca làm việc đang mở của nhân viên này
            KetToanCa caHienTai = ketToanCaRepository.timCaDangMoCuaNhanVien(maNV);

            if (caHienTai != null) {
                // Cập nhật thông tin vào ca hiện tại (thay vì tạo ca mới)
                caHienTai.setTongDoanhThu(tongTien);
                caHienTai.setNgayDongCa(java.time.LocalDate.now());
                caHienTai.setGioDongCa(java.time.LocalTime.now());

                ketToanCaRepository.save(caHienTai);
                return "OK";
            } else {
                return "Lỗi: Không tìm thấy ca làm việc nào đang mở để đóng!";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi: " + e.getMessage();
        }
    }
}