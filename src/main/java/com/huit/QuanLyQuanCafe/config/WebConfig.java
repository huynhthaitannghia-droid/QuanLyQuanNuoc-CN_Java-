package com.huit.QuanLyQuanCafe.config;

import com.huit.QuanLyQuanCafe.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Đăng ký bộ chặn bảo vệ hệ thống
        registry.addInterceptor(authInterceptor)
                // 1. CHẶN TOÀN BỘ: Áp dụng Interceptor cho mọi ngóc ngách của website
                .addPathPatterns("/**")

                // 2. NGOẠI LỆ (Không chặn): Chỉ mở cửa cho những trang công khai
                .excludePathPatterns(
                        "/",               // Trang chủ
                        "/xac-thuc",       // Hàm xử lý khi bấm đăng nhập
                        "/login",          // Đăng nhập
                        "/logout",         // Đăng xuất
                        "/css/**", "/js/**", "/images/**", "/webjars/**" // File tĩnh
                );
    }
}