package com.huit.QuanLyQuanCafe.controller;

import com.huit.QuanLyQuanCafe.entity.NhanVien;
import com.huit.QuanLyQuanCafe.repository.NhanVienRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private NhanVienRepository nhanVienRepository;

    // 1. Hiển thị trang đăng nhập (Trang chủ mặc định)
    @GetMapping("/")
    public String hienThiTrangDangNhap() {
        return "login";
    }

    // 2. Xử lý khi bấm nút Đăng Nhập
    @PostMapping("/xac-thuc")
    public String xuLyDangNhap(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               HttpSession session,
                               Model model) {

        // 1. Tìm nhân viên dưới DB theo tên đăng nhập
        NhanVien nv = nhanVienRepository.findByTenDangNhap(username);

        // 2. Kiểm tra tài khoản có tồn tại không và mật khẩu có khớp không
        if (nv != null && nv.getMatKhau().equals(password)) {

            // 3. Kiểm tra trạng thái (1 là đang làm, 0 là đã nghỉ - dựa theo schema SQL của bạn)
            if (nv.getTrangThai() == 0) {
                model.addAttribute("error", "Tài khoản này đã bị khóa hoặc nhân viên đã nghỉ việc!");
                return "login";
            }

            // 4. Đăng nhập thành công -> Lưu thông tin vào Session (Bộ nhớ phiên)
            session.setAttribute("maNV", nv.getMaNV()); // Lưu mã NV để mốt gắn vào lúc tạo Hóa đơn
            session.setAttribute("tenNV", nv.getTenNV());
            session.setAttribute("vaiTro", nv.getVaiTro());

            // 5. Phân quyền chuyển trang dựa theo Vai Trò
            if ("Admin".equalsIgnoreCase(nv.getVaiTro())) {
                return "redirect:/san-pham";
            } else {
                return "redirect:/hoa-don/ban-hang";
            }

        } else {
            // Sai tài khoản hoặc mật khẩu
            model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
            return "login";
        }
    }
}