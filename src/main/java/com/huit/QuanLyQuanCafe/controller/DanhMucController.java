package com.huit.QuanLyQuanCafe.controller;

import com.huit.QuanLyQuanCafe.entity.DanhMuc;
import com.huit.QuanLyQuanCafe.repository.DanhMucRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/danh-muc") // Đường dẫn gốc cho mọi thao tác với Danh Mục
public class DanhMucController {

    @Autowired
    private DanhMucRepository danhMucRepository;

    // 1. HIỂN THỊ DANH SÁCH & TÌM KIẾM
    @GetMapping
    public String hienThiDanhSach(Model model, @RequestParam(name = "keyword", required = false) String keyword) {
        // Nếu có từ khóa tìm kiếm
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("listDM", danhMucRepository.findByTenDMContainingIgnoreCase(keyword));
            model.addAttribute("keyword", keyword); // Giữ lại từ khóa trên ô tìm kiếm
        } else {
            // Nếu không tìm kiếm thì hiển thị tất cả
            model.addAttribute("listDM", danhMucRepository.findAll());
        }
        // ĐÃ SỬA: Trả về file HTML nằm trong thư mục templates/admin/
        return "admin/danh-muc";
    }

    // 2. HIỂN THỊ FORM THÊM MỚI
    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("danhMuc", new DanhMuc());
        return "admin/danh-muc-form";
    }

    // 3. HIỂN THỊ FORM CẬP NHẬT
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Integer id, Model model) {
        DanhMuc dm = danhMucRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy mã DM: " + id));
        model.addAttribute("danhMuc", dm);
        return "admin/danh-muc-form";
    }

    // 4. LƯU DANH MỤC (Dùng chung cho cả Thêm và Sửa)
    @PostMapping("/luu")
    public String luuDanhMuc(@ModelAttribute("danhMuc") DanhMuc danhMuc) {
        danhMucRepository.save(danhMuc);
        return "redirect:/danh-muc";
    }

    // 5. XÓA DANH MỤC
    @GetMapping("/xoa/{id}")
    public String xoaDanhMuc(@PathVariable("id") Integer id) {
        try {
            danhMucRepository.deleteById(id);
        } catch (Exception e) {
            // Bắt lỗi nếu bạn lỡ tay xóa danh mục đang chứa Sản phẩm (Lỗi khóa ngoại)
            System.out.println("Không thể xóa danh mục vì đang có sản phẩm thuộc danh mục này!");
        }
        return "redirect:/danh-muc";
    }
}