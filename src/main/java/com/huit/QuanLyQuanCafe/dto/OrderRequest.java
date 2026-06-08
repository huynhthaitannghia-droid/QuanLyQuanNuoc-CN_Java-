package com.huit.QuanLyQuanCafe.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest {
    private Integer maBan;
    private BigDecimal tongTien;
    private String phuongThuc;
    private List<CartItem> items;

    @Data
    public static class CartItem {
        private Integer id;
        private Integer soLuong;
        private BigDecimal gia;
        private String tuyChon;
        private String luongDuong;
        private String luongDa;
        private List<Integer> toppingIds;
    }
}