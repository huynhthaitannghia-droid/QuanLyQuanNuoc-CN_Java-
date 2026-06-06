package com.huit.QuanLyQuanCafe.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "ChiTietHoaDon")
public class ChiTietHoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaCTHD")
    private Integer maCTHD;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "DonGia", nullable = false)
    private BigDecimal donGia;

    @Column(name = "LuongDuong", length = 20)
    private String luongDuong;

    @Column(name = "LuongDa", length = 20)
    private String luongDa;

    @Column(name = "ThanhTien", nullable = false)
    private BigDecimal thanhTien;

    // KẾT NỐI KHÓA NGOẠI THÔNG THƯỜNG
    @ManyToOne
    @JoinColumn(name = "MaHD", nullable = false)
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "MaSP", nullable = false)
    private SanPham sanPham;

    // QUAN HỆ NHIỀU - NHIỀU VỚI TOPPING (Tự động map qua bảng ChiTietHoaDon_Topping)
    @ManyToMany
    @JoinTable(
            name = "ChiTietHoaDon_Topping", // Tên bảng trung gian dưới MySQL
            joinColumns = @JoinColumn(name = "MaCTHD"), // Khóa ngoại trỏ ngược lại bảng này
            inverseJoinColumns = @JoinColumn(name = "MaTopping") // Khóa ngoại trỏ sang bảng Topping
    )
    private List<Topping> listToppings; // Danh sách các topping đi kèm của ly nước này
}