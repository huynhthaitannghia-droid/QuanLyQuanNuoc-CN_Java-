package com.huit.QuanLyQuanCafe.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "SanPham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaSP")
    private Integer maSP;

    @Column(name = "TenSP", nullable = false, length = 100)
    private String tenSP;

    @Column(name = "GiaBan", nullable = false)
    private BigDecimal giaBan;

    @Column(name = "TrangThai")
    private Integer trangThai;

    @Column(name = "HinhAnh")
    private String hinhAnh;

    // LIÊN KẾT KHÓA NGOẠI: Nhiều sản phẩm thuộc 1 Danh mục
    @ManyToOne
    @JoinColumn(name = "MaDM", nullable = false) // Tên cột khóa ngoại dưới MySQL
    private DanhMuc danhMuc;
}