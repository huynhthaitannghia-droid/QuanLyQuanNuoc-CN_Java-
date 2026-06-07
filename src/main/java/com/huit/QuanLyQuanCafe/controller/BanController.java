package com.huit.QuanLyQuanCafe.controller;

import com.huit.QuanLyQuanCafe.entity.Ban;
import com.huit.QuanLyQuanCafe.repository.BanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ban")
public class BanController {

    @Autowired
    private BanRepository banRepository;

    // 1. HIỂN THỊ DANH SÁCH (READ)
    @GetMapping
    public String hienThiDanhSach(Model model) {
        model.addAttribute("listBan", banRepository.findAll());
        // Trỏ về file templates/admin/ban.html
        return "admin/ban";
    }

    // 2. HIỂN THỊ FORM THÊM MỚI (CREATE)
    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("ban", new Ban());
        // Trỏ về file templates/admin/ban-form.html
        return "admin/ban-form";
    }

    // 3. HIỂN THỊ FORM CẬP NHẬT (UPDATE)
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Integer id, Model model) {
        Ban ban = banRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy mã Bàn: " + id));
        model.addAttribute("ban", ban);
        // Trỏ về file templates/admin/ban-form.html
        return "admin/ban-form";
    }

    // 4. XỬ LÝ LƯU (Thêm & Sửa)
    @PostMapping("/luu")
    public String luuBan(@ModelAttribute("ban") Ban ban) {
        if (ban.getMaBan() != null) {
            // Trường hợp SỬA: Phải giữ nguyên trạng thái cũ của bàn dưới DB
            Ban banCu = banRepository.findById(ban.getMaBan()).orElse(null);
            if (banCu != null) {
                ban.setTrangThai(banCu.getTrangThai());
            }
        } else {
            // Trường hợp THÊM MỚI: Mặc định trạng thái luôn là "Trống"
            ban.setTrangThai("Trống");
        }

        banRepository.save(ban);
        return "redirect:/ban";
    }

    // 5. XỬ LÝ XÓA (DELETE)
    @GetMapping("/xoa/{id}")
    public String xoaBan(@PathVariable("id") Integer id) {
        banRepository.deleteById(id);
        return "redirect:/ban";
    }
}