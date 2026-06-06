package com.huit.QuanLyQuanCafe.repository;

import com.huit.QuanLyQuanCafe.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
}