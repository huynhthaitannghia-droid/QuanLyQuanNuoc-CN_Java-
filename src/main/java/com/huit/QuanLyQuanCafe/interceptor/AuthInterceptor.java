package com.huit.QuanLyQuanCafe.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object maNV = session.getAttribute("maNV");
        Object vaiTro = session.getAttribute("vaiTro"); // Đảm bảo lúc đăng nhập bạn có lưu vaiTro vào session nhé
        String uri = request.getRequestURI();

        // 1. KIỂM TRA ĐĂNG NHẬP CHUNG: Nếu chưa có Mã nhân viên trong session -> Bắt quay về trang Login
        if (maNV == null) {
            response.sendRedirect("/?error=Unauthenticated");
            return false; // Chặn lại, không cho đi tiếp vào Controller
        }

        // 2. ĐỊNH NGHĨA VÙNG CẤM: Gom tất cả các URL dành riêng cho Admin vào một nhóm
        boolean isAdminPage = uri.startsWith("/admin") ||
                uri.startsWith("/san-pham") ||
                uri.startsWith("/nhan-vien") ||
                uri.startsWith("/danh-muc") ||
                uri.startsWith("/ban") ||
                uri.startsWith("/topping");

        // 3. PHÂN QUYỀN CHI TIẾT: Nếu cố tình vào vùng cấm của Admin
        if (isAdminPage) {
            // Mà vai trò KHÔNG PHẢI là Admin (tức là Staff cố tình bypass) -> Đá về trang bán hàng
            if (!"Admin".equalsIgnoreCase((String) vaiTro)) {
                response.sendRedirect("/hoa-don/ban-hang?error=NoPermission");
                return false; // Chặn đứng tại đây
            }
        }

        return true; // Hợp lệ thì cho phép đi tiếp vào Controller
    }
}