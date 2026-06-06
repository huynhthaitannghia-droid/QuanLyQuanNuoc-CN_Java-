package com.huit.QuanLyQuanCafe.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest {
    private Integer maBan;
    private BigDecimal tongTien;
    private String phuongThuc;
    private List<CartItem> items; // Danh sách các món trong giỏ

    @Data
    public static class CartItem {
        private Integer id; // Mã Sản Phẩm
        private Integer soLuong;
        private BigDecimal gia;
    }
}