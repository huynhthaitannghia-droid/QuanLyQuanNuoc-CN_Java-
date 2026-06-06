package com.huit.QuanLyQuanCafe.controller;

import com.huit.QuanLyQuanCafe.entity.NhanVien;
import com.huit.QuanLyQuanCafe.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/nhan-vien")
public class NhanVienController {

    @Autowired
    private NhanVienRepository nhanVienRepository;

    // 1. HIỂN THỊ DANH SÁCH (READ)
    @GetMapping
    public String hienThiDanhSach(Model model) {
        model.addAttribute("listNV", nhanVienRepository.findAll());
        return "nhanvien/index";
    }

    // 2. HIỂN THỊ FORM THÊM MỚI (CREATE)
    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("nhanVien", new NhanVien());
        return "nhanvien/form";
    }

    // 3. HIỂN THỊ FORM CẬP NHẬT (UPDATE)
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Integer id, Model model) {
        NhanVien nv = nhanVienRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy mã NV: " + id));
        model.addAttribute("nhanVien", nv);
        return "nhanvien/form";
    }

    // 4. XỬ LÝ LƯU (Dùng chung cho cả Thêm và Sửa)
    @PostMapping("/luu")
    public String luuNhanVien(@ModelAttribute("nhanVien") NhanVien nhanVien) {
        nhanVienRepository.save(nhanVien);
        return "redirect:/nhan-vien";
    }

    // 5. XỬ LÝ XÓA (DELETE)
    @GetMapping("/xoa/{id}")
    public String xoaNhanVien(@PathVariable("id") Integer id) {
        nhanVienRepository.deleteById(id);
        return "redirect:/nhan-vien";
    }
}