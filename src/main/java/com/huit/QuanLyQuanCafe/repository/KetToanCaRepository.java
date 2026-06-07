package com.huit.QuanLyQuanCafe.repository;

import com.huit.QuanLyQuanCafe.entity.KetToanCa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface KetToanCaRepository extends JpaRepository<KetToanCa, Integer> {

    // Bật nativeQuery = true để Spring Boot hiểu đây là câu lệnh MySQL thuần (được phép dùng LIMIT)
    @Query(value = "SELECT * FROM kettoanca WHERE MaNV = ?1 AND GioDongCa IS NULL ORDER BY Id DESC LIMIT 1", nativeQuery = true)
    KetToanCa timCaDangMoCuaNhanVien(Integer maNV);

}