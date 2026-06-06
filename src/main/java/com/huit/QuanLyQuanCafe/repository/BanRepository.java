package com.huit.QuanLyQuanCafe.repository;

import com.huit.QuanLyQuanCafe.entity.Ban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BanRepository extends JpaRepository<Ban, Integer> {
}