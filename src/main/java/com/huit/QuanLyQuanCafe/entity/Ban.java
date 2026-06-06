package com.huit.QuanLyQuanCafe.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Ban")
public class Ban {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaBan")
    private Integer maBan;

    @Column(name = "TenBan", nullable = false, length = 50)
    private String tenBan;

    @Column(name = "TrangThai", length = 20)
    private String trangThai; // 'Trống' hoặc 'Có khách'
}