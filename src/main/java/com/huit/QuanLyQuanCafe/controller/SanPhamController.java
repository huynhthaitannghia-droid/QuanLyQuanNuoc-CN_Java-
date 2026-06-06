package com.huit.QuanLyQuanCafe.controller;

import com.huit.QuanLyQuanCafe.entity.SanPham;
import com.huit.QuanLyQuanCafe.repository.DanhMucRepository;
import com.huit.QuanLyQuanCafe.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/san-pham")
public class SanPhamController {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private DanhMucRepository danhMucRepository;

    // 1. HIỂN THỊ DANH SÁCH (READ)
    @GetMapping
    public String hienThiDanhSach(Model model) {
        model.addAttribute("listSP", sanPhamRepository.findAll());
        return "sanpham/index";
    }

    // 2. HIỂN THỊ FORM THÊM MỚI (CREATE)
    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("sanPham", new SanPham());
        model.addAttribute("listDM", danhMucRepository.findAll()); // Load danh mục vào CBB
        return "sanpham/form";
    }

    // 3. HIỂN THỊ FORM CẬP NHẬT (UPDATE)
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Integer id, Model model) {
        SanPham sp = sanPhamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy mã SP: " + id));
        model.addAttribute("sanPham", sp);
        model.addAttribute("listDM", danhMucRepository.findAll());
        return "sanpham/form";
    }

    // 4. XỬ LÝ LƯU (Dùng chung cho cả Thêm mới và Cập nhật)
    @PostMapping("/luu")
    public String luuSanPham(@ModelAttribute("sanPham") SanPham sanPham) {
        sanPhamRepository.save(sanPham);
        return "redirect:/san-pham"; // Lưu xong quay về trang danh sách
    }

    // 5. XỬ LÝ XÓA (DELETE)
    @GetMapping("/xoa/{id}")
    public String xoaSanPham(@PathVariable("id") Integer id) {
        sanPhamRepository.deleteById(id);
        return "redirect:/san-pham";
    }
}