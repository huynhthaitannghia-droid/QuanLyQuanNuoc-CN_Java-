package com.huit.QuanLyQuanCafe.controller;

import com.huit.QuanLyQuanCafe.repository.ToppingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/topping")
public class ToppingController {
    @Autowired
    private ToppingRepository toppingRepository;

    @GetMapping
    public String hienThiDanhSach(Model model) {
        model.addAttribute("listTopping", toppingRepository.findAll());
        return "topping/index";
    }
}