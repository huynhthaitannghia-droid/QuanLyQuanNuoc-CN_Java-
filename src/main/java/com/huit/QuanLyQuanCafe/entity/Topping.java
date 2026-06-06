package com.huit.QuanLyQuanCafe.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "Topping")
public class Topping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTopping")
    private Integer maTopping;

    @Column(name = "TenTopping", nullable = false, unique = true, length = 100)
    private String tenTopping;

    @Column(name = "GiaBan", nullable = false)
    private BigDecimal giaBan; // Dùng BigDecimal cho tiền tệ để chính xác nhất
}