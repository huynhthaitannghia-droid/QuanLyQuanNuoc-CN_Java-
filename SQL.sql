-- =========================================================
-- KHỞI TẠO DATABASE TỪ ĐẦU (Xóa cũ tạo mới cho sạch sẽ)
-- =========================================================
DROP DATABASE IF EXISTS QuanLyQuanCafe;
CREATE DATABASE QuanLyQuanCafe DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE QuanLyQuanCafe;

-- ==========================================
-- PHẦN 1: TẠO CÁC BẢNG DANH MỤC ĐỘC LẬP
-- ==========================================

-- 1. Bảng Nhân Viên
CREATE TABLE NhanVien (
    MaNV INT AUTO_INCREMENT PRIMARY KEY,
    TenNV VARCHAR(100) NOT NULL,
    SoDienThoai VARCHAR(15),
    VaiTro VARCHAR(20) NOT NULL, 
    TenDangNhap VARCHAR(50) UNIQUE NOT NULL,
    MatKhau VARCHAR(255) NOT NULL,
    TrangThai TINYINT DEFAULT 1 
);

-- 2. Bảng Danh Mục Sản Phẩm
CREATE TABLE DanhMuc (
    MaDM INT AUTO_INCREMENT PRIMARY KEY,
    TenDM VARCHAR(100) NOT NULL UNIQUE
);

-- 3. Bảng Topping 
CREATE TABLE Topping (
    MaTopping INT AUTO_INCREMENT PRIMARY KEY,
    TenTopping VARCHAR(100) NOT NULL UNIQUE,
    GiaBan DECIMAL(10,2) NOT NULL DEFAULT 0
);

-- 4. Bảng Bàn 
CREATE TABLE Ban (
    MaBan INT AUTO_INCREMENT PRIMARY KEY,
    TenBan VARCHAR(50) NOT NULL,
    TrangThai VARCHAR(20) DEFAULT 'Trống' 
);

-- ==========================================
-- PHẦN 2: TẠO CÁC BẢNG CÓ LIÊN KẾT
-- ==========================================

-- 5. Bảng Sản Phẩm
CREATE TABLE SanPham (
    MaSP INT AUTO_INCREMENT PRIMARY KEY,
    TenSP VARCHAR(100) NOT NULL,
    MaDM INT NOT NULL,
    GiaBan DECIMAL(10,2) NOT NULL,
    TrangThai TINYINT DEFAULT 1, 
    HinhAnh VARCHAR(255),
    FOREIGN KEY (MaDM) REFERENCES DanhMuc(MaDM) ON UPDATE CASCADE
);

-- 6. Bảng Hóa Đơn
CREATE TABLE HoaDon (
    MaHD INT AUTO_INCREMENT PRIMARY KEY,
    MaBan INT,
    MaNV INT NOT NULL,
    NgayTao DATE NOT NULL,
    GioTao TIME NOT NULL,
    TongTien DECIMAL(10,2) DEFAULT 0,
    LoaiDon VARCHAR(50) DEFAULT 'Tại chỗ',
    PhuThu DECIMAL(10,2) DEFAULT 0,
    PhuongThucThanhToan VARCHAR(50) DEFAULT 'Tiền mặt', 
    TrangThai TINYINT DEFAULT 0, 
    FOREIGN KEY (MaBan) REFERENCES Ban(MaBan) ON DELETE SET NULL,
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
);

-- 7. Bảng Chi Tiết Hóa Đơn 
CREATE TABLE ChiTietHoaDon (
    MaCTHD INT AUTO_INCREMENT PRIMARY KEY,
    MaHD INT NOT NULL,
    MaSP INT NOT NULL,
    SoLuong INT NOT NULL,
    DonGia DECIMAL(10,2) NOT NULL, 
    LuongDuong VARCHAR(20) DEFAULT '100% đường', 
    LuongDa VARCHAR(20) DEFAULT 'Đá chung',      
    ThanhTien DECIMAL(10,2) NOT NULL, 
    FOREIGN KEY (MaHD) REFERENCES HoaDon(MaHD) ON DELETE CASCADE,
    FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP)
);

-- 8. Bảng Chi Tiết Hóa Đơn - Topping 
CREATE TABLE ChiTietHoaDon_Topping (
    MaCTHD INT NOT NULL,
    MaTopping INT NOT NULL,
    PRIMARY KEY (MaCTHD, MaTopping),
    FOREIGN KEY (MaCTHD) REFERENCES ChiTietHoaDon(MaCTHD) ON DELETE CASCADE,
    FOREIGN KEY (MaTopping) REFERENCES Topping(MaTopping) ON DELETE CASCADE
);

-- 9. Bảng Kết Toán Ca (ĐÃ CẬP NHẬT MỞ CA LÀM VIỆC)
CREATE TABLE KetToanCa (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    NgayMoCa DATE NOT NULL,
    GioMoCa TIME NOT NULL,
    TienDauCa DECIMAL(10,2) DEFAULT 0,
    TongDoanhThu DECIMAL(10,2) DEFAULT 0,
    NgayDongCa DATE, -- Cho phép NULL vì lúc mở ca chưa đóng
    GioDongCa TIME,  -- Cho phép NULL
    MaNV INT NOT NULL,
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
);

-- =========================================================
-- PHẦN 3: THÊM DỮ LIỆU MẪU
-- =========================================================

-- Thêm Nhân Viên
INSERT INTO NhanVien (TenNV, SoDienThoai, VaiTro, TenDangNhap, MatKhau) VALUES 
('Nguyễn Quản Lý', '0901234567', 'Admin', 'admin', '123'),
('Trần Thu Ngân', '0987654321', 'Staff', 'staff', '123');

-- Thêm Danh Mục
INSERT INTO DanhMuc (TenDM) VALUES ('Cà phê'), ('Trà sữa'), ('Trà trái cây');

-- Thêm Topping
INSERT INTO Topping (TenTopping, GiaBan) VALUES 
('Trân châu đen', 5000), ('Trân châu trắng', 5000), 
('Sương sáo', 8000), ('Pudding', 8000);

-- Thêm Bàn (Đã sửa lỗi và thêm đủ slot mang về, ship, grab)
INSERT INTO Ban (TenBan) VALUES 
('Bàn 1'), ('Bàn 2'), ('Bàn 3'), ('Bàn 4'), ('Bàn 5'), 
('Mang về 1'), ('Mang về 2'), ('Mang về 3'), ('Mang về 4'), ('Mang về 5'), 
('Ship 1'), ('Ship 2'), ('Ship 3'), ('Ship 4'), ('Ship 5'), ('G1'), ('G2'), ('G3'), ('G4'), ('G5');

-- Thêm Sản Phẩm 
INSERT INTO SanPham (TenSP, MaDM, GiaBan, HinhAnh) VALUES 
('Cà phê sữa đá', 1, 29000, '/images/CafeSua.jpg'),
('Bạc xỉu', 1, 32000, '/images/bacsiu.jpg'),
('Cà phê đen', 1, 25000, '/images/CafeDen.jpg'),
('Trà sữa trân châu', 2, 39000, '/images/TraSuaChanTrau.jpg'),
('Trà đào cam sả', 3, 45000, '/images/TraDao.jpg');