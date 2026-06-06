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

    // 1. DANH SÁCH & TÌM KIẾM
    @GetMapping
    public String hienThiDanhSach(Model model, @RequestParam(name = "keyword", required = false) String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("listNV", nhanVienRepository.findByTenNVContainingIgnoreCase(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("listNV", nhanVienRepository.findAll());
        }
        return "admin/nhan-vien";
    }

    // 2. FORM THÊM MỚI
    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("nhanVien", new NhanVien());
        return "admin/nhan-vien-form";
    }

    // 3. FORM SỬA (Lưu ý kiểu dữ liệu của ID)
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Integer id, Model model) {
        NhanVien nv = nhanVienRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy mã NV: " + id));
        model.addAttribute("nhanVien", nv);
        return "admin/nhan-vien-form";
    }

    // 4. LƯU
    @PostMapping("/luu")
    public String luuNhanVien(@ModelAttribute("nhanVien") NhanVien nhanVien) {

        // Kiểm tra trạng thái: Nếu là nhân viên mới thì set mặc định = 1
        if (nhanVien.getTrangThai() == null) {
            nhanVien.setTrangThai(1);
        }

        nhanVienRepository.save(nhanVien);
        return "redirect:/nhan-vien";
    }

    // 5. XÓA
    @GetMapping("/xoa/{id}")
    public String xoaNhanVien(@PathVariable("id") Integer id) {
        // Tìm xem nhân viên có tồn tại không trước khi xóa
        NhanVien nv = nhanVienRepository.findById(id).orElse(null);
        if (nv != null) {
            nhanVienRepository.delete(nv);
        }
        return "redirect:/nhan-vien";
    }

    // 6. ĐỔI TRẠNG THÁI (BẬT/TẮT)
    @GetMapping("/doi-trang-thai/{id}")
    public String doiTrangThai(@PathVariable("id") Integer id) {
        if (id == 1) {
            return "redirect:/nhan-vien?error=cannot_lock_admin";
        }
        NhanVien nv = nhanVienRepository.findById(id).orElse(null);
        if (nv != null) {
            // Nếu là 1 thì chuyển thành 0, ngược lại thì thành 1
            int trangThaiMoi = (nv.getTrangThai() == 1) ? 0 : 1;
            nv.setTrangThai(trangThaiMoi);
            nhanVienRepository.save(nv);
        }
        return "redirect:/nhan-vien";
    }
}