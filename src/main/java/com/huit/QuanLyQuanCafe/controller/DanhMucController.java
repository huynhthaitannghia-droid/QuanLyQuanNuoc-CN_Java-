package com.huit.QuanLyQuanCafe.controller;

import com.huit.QuanLyQuanCafe.repository.DanhMucRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/danh-muc") // Đường dẫn gốc cho mọi thao tác với Danh Mục
public class DanhMucController {

    @Autowired
    private DanhMucRepository danhMucRepository;

    @GetMapping
    public String hienThiDanhSach(Model model) {
        model.addAttribute("listDM", danhMucRepository.findAll());
        // Trả về file HTML nằm trong thư mục templates/danhmuc/index.html (sau này sẽ tạo)
        return "danhmuc/index";
    }
}