package com.huit.QuanLyQuanCafe.controller;

import com.huit.QuanLyQuanCafe.entity.Topping;
import com.huit.QuanLyQuanCafe.repository.ToppingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/topping")
public class ToppingController {

    @Autowired
    private ToppingRepository toppingRepository;

    // 1. HIỂN THỊ DANH SÁCH (READ)
    @GetMapping
    public String hienThiDanhSach(Model model) {
        model.addAttribute("listTopping", toppingRepository.findAll());
        // Trỏ về file templates/admin/topping.html
        return "admin/topping";
    }

    // 2. HIỂN THỊ FORM THÊM MỚI (CREATE)
    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("topping", new Topping());
        // Trỏ về file templates/admin/topping-form.html
        return "admin/topping-form";
    }

    // 3. HIỂN THỊ FORM CẬP NHẬT (UPDATE)
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Integer id, Model model) {
        Topping topping = toppingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy mã Topping: " + id));
        model.addAttribute("topping", topping);
        // Trỏ về file templates/admin/topping-form.html
        return "admin/topping-form";
    }

    // 4. XỬ LÝ LƯU (Thêm & Sửa)
    @PostMapping("/luu")
    public String luuTopping(@ModelAttribute("topping") Topping topping) {
        toppingRepository.save(topping);
        return "redirect:/topping";
    }

    // 5. XỬ LÝ XÓA (DELETE)
    @GetMapping("/xoa/{id}")
    public String xoaTopping(@PathVariable("id") Integer id) {
        toppingRepository.deleteById(id);
        return "redirect:/topping";
    }
}