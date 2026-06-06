package com.huit.QuanLyQuanCafe.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "HoaDon")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHD")
    private Integer maHD;

    @Column(name = "NgayTao")
    private java.time.LocalDate ngayTao;

    @Column(name = "GioTao")
    private java.time.LocalTime gioTao;

    @Column(name = "TongTien")
    private BigDecimal tongTien;

    @Column(name = "LoaiDon", length = 50)
    private String loaiDon;

    @Column(name = "PhuThu")
    private BigDecimal phuThu;

    @Column(name = "PhuongThucThanhToan", length = 50)
    private String phuongThucThanhToan;

    @Column(name = "TrangThai")
    private Integer trangThai;

    // CÁC MỐI QUAN HỆ KHÓA NGOẠI
    @ManyToOne
    @JoinColumn(name = "MaBan")
    private Ban ban;

    @ManyToOne
    @JoinColumn(name = "MaNV", nullable = false)
    private NhanVien nhanVien; 
}