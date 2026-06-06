package com.huit.QuanLyQuanCafe.controller;

import com.huit.QuanLyQuanCafe.entity.SanPham;
import com.huit.QuanLyQuanCafe.repository.DanhMucRepository;
import com.huit.QuanLyQuanCafe.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/san-pham")
public class SanPhamController {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private DanhMucRepository danhMucRepository;

    // 1. HIỂN THỊ DANH SÁCH & TÌM KIẾM
    @GetMapping
    public String hienThiDanhSach(Model model, @RequestParam(name = "keyword", required = false) String keyword) {

        // Nếu người dùng có gõ từ khóa -> Gọi hàm tìm kiếm
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("listSP", sanPhamRepository.findByTenSPContainingIgnoreCase(keyword));
            model.addAttribute("keyword", keyword); // Đẩy lại từ khóa lên View để giữ chữ trong ô nhập
        }
        // Nếu không gõ gì -> Hiển thị tất cả như cũ
        else {
            model.addAttribute("listSP", sanPhamRepository.findAll());
        }

        return "admin/san-pham";
    }

    // 2. HIỂN THỊ FORM THÊM MỚI
    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("sanPham", new SanPham());
        model.addAttribute("listDM", danhMucRepository.findAll());
        // SỬA CHỖ NÀY: Trỏ về file form mới tạo
        return "admin/san-pham-form";
    }

    // 3. HIỂN THỊ FORM CẬP NHẬT
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Integer id, Model model) {
        SanPham sp = sanPhamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy mã SP: " + id));
        model.addAttribute("sanPham", sp);
        model.addAttribute("listDM", danhMucRepository.findAll());
        // SỬA CHỖ NÀY: Trỏ về file form
        return "admin/san-pham-form";
    }

    // 4. XỬ LÝ LƯU (Dùng chung cho cả Thêm mới và Cập nhật)
    @PostMapping("/luu")
    public String luuSanPham(@ModelAttribute("sanPham") SanPham sanPham,
                             @RequestParam(value = "fileAnh", required = false) MultipartFile fileAnh) {
        try {
            // Kiểm tra xem người dùng có chọn file ảnh mới không
            if (fileAnh != null && !fileAnh.isEmpty()) {
                // Lấy tên file gốc (VD: tra-sua.jpg)
                String fileName = fileAnh.getOriginalFilename();

                // Chỉ định thư mục lưu ảnh trong project (Nằm trong src/main/resources/static/images/)
                String uploadDir = "src/main/resources/static/images/";
                Path uploadPath = Paths.get(uploadDir);

                // Nếu thư mục chưa có thì tự động tạo mới
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Copy file từ form vào ổ cứng
                try (InputStream inputStream = fileAnh.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }

                // Lưu đường dẫn vào Entity để gọi lên HTML (VD: /images/tra-sua.jpg)
                sanPham.setHinhAnh("/images/" + fileName);

            } else {
                // TRƯỜNG HỢP UPDATE: Nếu không chọn ảnh mới, phải giữ lại đường dẫn ảnh cũ!
                if (sanPham.getMaSP() != null) {
                    SanPham spCu = sanPhamRepository.findById(sanPham.getMaSP()).orElse(null);
                    if (spCu != null) {
                        sanPham.setHinhAnh(spCu.getHinhAnh());
                    }
                }
            }

            // Cuối cùng là lưu toàn bộ thông tin xuống Database
            sanPhamRepository.save(sanPham);

        } catch (Exception e) {
            System.out.println("LỖI LƯU ẢNH: " + e.getMessage());
        }

        return "redirect:/san-pham";
    }

    // 5. XỬ LÝ XÓA
    @GetMapping("/xoa/{id}")
    public String xoaSanPham(@PathVariable("id") Integer id) {
        sanPhamRepository.deleteById(id);
        return "redirect:/san-pham";
    }
}