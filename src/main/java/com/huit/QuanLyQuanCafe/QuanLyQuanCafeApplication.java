package com.huit.QuanLyQuanCafe;

import com.huit.QuanLyQuanCafe.entity.DanhMuc;
import com.huit.QuanLyQuanCafe.repository.DanhMucRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class QuanLyQuanCafeApplication implements CommandLineRunner {

    // "Tiêm" (Inject) kho vũ khí DanhMucRepository vào để sử dụng
    @Autowired
    private DanhMucRepository danhMucRepository;

    public static void main(String[] args) {
        SpringApplication.run(QuanLyQuanCafeApplication.class, args);
    }

    // Hàm run này sẽ tự động chạy ngay sau khi ứng dụng Spring Boot khởi động xong
    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== KIỂM TRA KẾT NỐI DATABASE ===");

        // CHỈ 1 DÒNG LỆNH: Lấy toàn bộ dữ liệu từ bảng DanhMuc
        List<DanhMuc> danhSach = danhMucRepository.findAll();

        if (danhSach.isEmpty()) {
            System.out.println("Bảng Danh Mục dưới Database đang trống!");
        } else {
            for (DanhMuc dm : danhSach) {
                System.out.println(dm); // Nhờ @Data của Lombok, nó sẽ tự in đẹp ra màn hình
            }
        }
        System.out.println("=================================\n");
    }
}