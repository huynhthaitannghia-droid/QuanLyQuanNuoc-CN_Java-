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
        try {
            NhanVien nv = nhanVienRepository.findByTenDangNhap(username);

            if (nv != null && nv.getMatKhau().equals(password)) {
                // Kiểm tra null an toàn (tránh văng NullPointerException)
                if (nv.getTrangThai() == null || nv.getTrangThai() != 1) {
                    model.addAttribute("error", "Tài khoản này đã bị khóa hoặc chưa kích hoạt!");
                    return "login";
                }

                session.setAttribute("maNV", nv.getMaNV());
                session.setAttribute("tenNV", nv.getTenNV());
                session.setAttribute("vaiTro", nv.getVaiTro());

                return "Admin".equalsIgnoreCase(nv.getVaiTro()) ? "redirect:/admin/dashboard" : "redirect:/hoa-don/ban-hang";
            } else {
                model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
                return "login";
            }
        } catch (Exception e) {
            // Thay vì để web sập, ta bắt lấy lỗi và trả về trang đăng nhập với thông báo
            model.addAttribute("error", "Hệ thống đang bận, vui lòng thử lại sau!");
            return "login";
        }
    }

    // ==========================================
    // 3. XỬ LÝ ĐĂNG XUẤT (THÊM MỚI Ở ĐÂY)
    // ==========================================
    @GetMapping("/logout")
    public String xuLyDangXuat(HttpSession session) {
        // Lệnh này sẽ xóa sạch trí nhớ của Server về người dùng hiện tại
        session.invalidate();

        // Sau khi xóa xong thì đá thẳng về trang chủ đăng nhập
        return "redirect:/";
    }
}