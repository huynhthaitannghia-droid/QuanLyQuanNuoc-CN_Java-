package com.huit.QuanLyQuanCafe.controller;

import com.huit.QuanLyQuanCafe.repository.ChiTietHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chi-tiet-hoa-don")
public class ChiTietHoaDonController {
    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

    @GetMapping
    public String hienThiDanhSach(Model model) {
        model.addAttribute("listCTHD", chiTietHoaDonRepository.findAll());
        return "chitiethoadon/index";
    }
}