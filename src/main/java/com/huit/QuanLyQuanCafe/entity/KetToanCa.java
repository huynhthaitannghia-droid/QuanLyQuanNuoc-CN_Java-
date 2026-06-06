package com.huit.QuanLyQuanCafe.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "KetToanCa")
public class KetToanCa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "TongDoanhThu")
    private BigDecimal tongDoanhThu;

    // --- 2 CỘT MỚI TÁCH RA ---
    @Column(name = "NgayDongCa")
    private LocalDate ngayDongCa;

    @Column(name = "GioDongCa")
    private LocalTime gioDongCa;
    // -------------------------

    @Column(name = "MaNV")
    private Integer maNhanVien;
}