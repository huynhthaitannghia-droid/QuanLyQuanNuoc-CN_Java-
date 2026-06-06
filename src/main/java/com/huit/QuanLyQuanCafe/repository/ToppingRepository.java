package com.huit.QuanLyQuanCafe.repository;

import com.huit.QuanLyQuanCafe.entity.Topping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToppingRepository extends JpaRepository<Topping, Integer> {
}