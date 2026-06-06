package com.huit.QuanLyQuanCafe.repository;

import com.huit.QuanLyQuanCafe.entity.KetToanCa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KetToanCaRepository extends JpaRepository<KetToanCa, Integer> {
}