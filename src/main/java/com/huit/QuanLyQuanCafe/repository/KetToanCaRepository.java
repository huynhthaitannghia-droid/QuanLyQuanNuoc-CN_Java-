package com.huit.QuanLyQuanCafe.repository;

import com.huit.QuanLyQuanCafe.entity.KetToanCa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface KetToanCaRepository extends JpaRepository<KetToanCa, Integer> {

}