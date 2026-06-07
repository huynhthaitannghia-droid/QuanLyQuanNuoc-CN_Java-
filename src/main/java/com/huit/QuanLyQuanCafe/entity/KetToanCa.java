package com.huit.QuanLyQuanCafe.entity;

import jakarta.persistence.*;
import lombok.Data;
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

    // Đổi thành Double cho khớp với Controller và dễ tính toán cộng trừ
    @Column(name = "TongDoanhThu")
    private Double tongDoanhThu;

    @Column(name = "NgayDongCa")
    private LocalDate ngayDongCa;

    @Column(name = "GioDongCa")
    private LocalTime gioDongCa;

    // --- 3 CỘT MỚI BỔ SUNG ĐỂ MỞ CA LÀM VIỆC ---
    @Column(name = "NgayMoCa")
    private LocalDate ngayMoCa;

    @Column(name = "GioMoCa")
    private LocalTime gioMoCa;

    @Column(name = "TienDauCa")
    private Double tienDauCa;
    // -------------------------------------------

    @Column(name = "MaNV")
    private Integer maNhanVien;
}