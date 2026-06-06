package com.huit.QuanLyQuanCafe.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data //Lombok: Tự động sinh ra các hàm Getter, Setter, Constructor ẩn bên dưới
@Entity // Báo cho Spring Boot biết class này sẽ ánh xạ với 1 bảng dưới Database
@Table(name = "DanhMuc") // Chỉ định đích danh tên bảng trong MySQL
public class DanhMuc {

    @Id // Đánh dấu thuộc tính này là Khóa chính (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Khóa chính tự động tăng (AUTO_INCREMENT)
    @Column(name = "MaDM") // Tên cột dưới database
    private Integer maDM;

    @Column(name = "TenDM", nullable = false, unique = true, length = 100)
    private String tenDM;
}