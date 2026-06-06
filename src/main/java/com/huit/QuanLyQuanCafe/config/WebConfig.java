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
                // Các đường dẫn BẮT BUỘC phải đăng nhập mới được vào
                .addPathPatterns("/admin/**", "/san-pham/**", "/hoa-don/**")

                // Các đường dẫn NGOẠI LỆ (Không chặn)
                .excludePathPatterns("/", "/login", "/logout") // Trang đăng nhập gốc công cộng
                .excludePathPatterns("/css/**", "/js/**", "/images/**", "/webjars/**"); // Các file giao diện tĩnh
    }
}