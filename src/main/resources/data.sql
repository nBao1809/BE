-- XÓA DỮ LIỆU CŨ TRƯỚC KHI TẠO MOCKUP
DELETE FROM service_request;
DELETE FROM service_type;
DELETE FROM payment;
DELETE FROM feedback;
DELETE FROM order_detail;
DELETE FROM orders;
DELETE FROM employee;
DELETE FROM session;
DELETE FROM customer;
DELETE FROM tables;
delete from dish_option;
DELETE FROM option_detail;
DELETE FROM dish;
DELETE FROM option_type;
DELETE FROM category;



-- MOCKUP DATA
-- CATEGORY
INSERT INTO category (id, name) VALUES
(1, 'Thịt nướng'),
(2, 'Hải sản'),
(3, 'Cơm'),
(4, 'Mì'),
(5, 'Nước');

INSERT INTO dish (id, name, price, image_url, status, category_id) VALUES
-- Thịt nướng
(1, 'Ba chỉ bò Mỹ', 180000, 'https://product.hstatic.net/200000922775/product/2_871bbb06265e43ff8531dd015911081a_grande.jpg', 1, 1),
(2, 'Dẻ sườn bò Mỹ nướng', 240000, 'https://superfoods.vn/wp-content/uploads/2023/08/Kham-pha-huong-vi-hap-dan-voi-de-suon-bo-My-nuong-01-1.jpg', 1, 1),
(3, 'Ba chỉ heo tẩm sốt cay', 150000, 'https://www.huongnghiepaau.com/wp-content/uploads/2016/06/ba-roi-uop-cung-xot.jpg', 1, 1),
(4, 'Nạc vai bò Mỹ', 200000, 'https://namkhaiphu.com/wp-content/uploads/2019/07/DSC_4419.jpg', 1, 1),
(5, 'Sườn non bò nướng sốt mật ong', 220000, 'https://www.lorca.vn/wp-content/uploads/2021/05/cach-lam-suon-nuong-mat-ong-bang-noi-chien-khong-dau.jpg', 1, 1),
(6, 'Thăn bò Úc nướng muối tiêu', 250000, 'https://thitngonnhapkhau.vn/wp-content/uploads/2022/12/thit-bo-nuong-tang.jpg', 1, 1),

-- Hải sản
(7, 'Tôm sú nướng muối ớt', 180000, 'https://nhahanghaisanlangchai.com/upload/images/tom_su_nuong_muoi_ot.png', 1, 2),
(8, 'Bạch tuộc nướng sa tế', 160000, 'https://cdn.tgdd.vn/Files/2021/08/25/1377746/cach-lam-bach-tuoc-nuong-sa-te-thom-ngon-don-gian-tai-nha-202201100040099065.jpeg', 1, 2),
(9, 'Hàu nướng mỡ hành', 120000, 'https://congthucgiadinh.com/storage/42/01J2GKZ23M67S56B2V8NHA4ZJR.jpg', 1, 2),
(10, 'Ngao hấp sả', 100000, 'https://cdn.tgdd.vn/Files/2020/06/29/1266322/cach-lam-ngao-hap-sa-ot-dam-da-nong-hoi-ca-nha-me-tit-202006291324239176.jpg', 1, 2),
(11, 'Càng cua sốt cay Hàn Quốc', 220000, 'https://pastaxi-manager.onepas.vn/content/uploads/articles/minhnguyet/cua-sot-ot/cach-lam-cua-sot-ot-singapore-1.jpg', 1, 2),
(12, 'Tôm hùm mini nướng phô mai', 300000, 'https://cdn.tgdd.vn/2020/07/CookProduct/30-1200x676-3.jpg', 1, 2),

-- Cơm
(13, 'Cơm chiên hải sản', 70000, 'https://cdn.eva.vn/upload/3-2023/images/2023-07-28/com-chien-hai-san-ngon-hap-dan-cach-lam-don-gian-nhat-5-1690517403-179-width605height416.jpg', 1, 3),
(14, 'Cơm bò lúc lắc', 90000, 'https://cdn.tgdd.vn/2021/01/CookProduct/thumb1-1200x676-10.jpg', 1, 3),
(15, 'Cơm gà xé', 60000, 'https://lh5.googleusercontent.com/proxy/XDSoAKIOOl4myF132sCcQErlV1k-FY4kSyYkfLd446yLLHDDU-ttTskwmXSdLi7_WnieodcQMWtX0Rh774wFJ6YtQFkLlxM1Qp-x2Q', 1, 3),
(16, 'Cơm bò tiêu đen', 85000, 'https://afamilycdn.com/zoom/700_438/E88MOq9iOdccccccccccccjjVmV53G/Image/2016/05/A-b2356.jpg', 1, 3),
(17, 'Cơm gà teriyaki', 95000, 'https://img-global.cpcdn.com/recipes/142a3f474964e5c0/1200x630cq80/photo.jpg', 1, 3),

-- Mì
(18, 'Mì xào hải sản', 80000, 'https://cdn.netspace.edu.vn/images/2021/07/18/cach-lam-mi-xao-hai-san-800.jpg', 1, 4),
(19, 'Mì xào bò', 75000, 'https://cdn.tgdd.vn/2021/12/CookDish/cach-lam-mi-goi-xao-thit-bo-don-gian-thom-ngon-hap-dan-ai-avt-1200x676.jpg', 1, 4),
(20, 'Mì ramen', 100000, 'https://satovietnhat.com.vn/Upload/images/anh-bia-mi-ramen-nhat-ban-1.png', 1, 4),
(21, 'Spaghetti sốt bò bằm', 120000, 'https://martfood.vn/wp-content/uploads/2022/01/my-y-spaghetti-sot-bo-bam.jpg', 1, 4),

-- Nước
(22, 'Nước cam tươi', 35000, 'https://cdn.tgdd.vn/2020/07/CookProductThumb/nuocscam-620x620.jpg', 1, 5),
(23, 'Nước ép táo', 40000, 'https://tandoorvietnam.com/wp-content/uploads/2020/06/Apple-juice1.jpg', 1, 5),
(24, '7up', 25000, 'https://www.lottemart.vn/media/catalog/product/cache/0x0/8/9/8934588022111-1-1.jpg.webp', 1, 5),
(25, 'Coca cola', 25000, 'https://product.hstatic.net/200000534989/product/dsc08341-enhanced-nr_1_e6d5d0a13c8f42c2bd7cea59e03ce199_master.jpg', 1, 5),
(26, 'Trà chanh', 30000, 'https://cdn.tgdd.vn/2021/05/CookRecipe/GalleryStep/thanh-pham-910.jpg', 1, 5);
-- Ba chỉ bò Mỹ (id=1) có thể chọn gia vị
INSERT INTO dish_option (dish_id, option_detail_id) VALUES
(1, 1), -- Ít cay
(1, 2), -- Không hành
(1, 3), -- Thêm tỏi
(1, 4); -- Thêm ớt

-- Dẻ sườn bò Mỹ nướng (id=2)
INSERT INTO dish_option (dish_id, option_detail_id) VALUES
(2, 1),
(2, 4);

-- Cơm chiên hải sản (id=13) có thể thêm trứng, phô mai
INSERT INTO dish_option (dish_id, option_detail_id) VALUES
(13, 15), -- Thêm trứng
(13, 17); -- Thêm phô mai

-- Nước cam tươi (id=22)
INSERT INTO dish_option (dish_id, option_detail_id) VALUES
(22, 5), -- Không đá
(22, 6), -- Đá ít
(22, 7), -- Đá nhiều
(22, 8), -- Không đường
(22, 9), -- Ít đường
(22, 10), -- Bình thường
(22, 11), -- Ngọt nhiều
(22, 12), -- Nóng
(22, 13), -- Ấm
(22, 14); -- Lạnh

-- Trà chanh (id=26) cũng có đủ đá, ngọt, nhiệt độ
INSERT INTO dish_option (dish_id, option_detail_id) VALUES
(26, 5),
(26, 6),
(26, 7),
(26, 8),
(26, 9),
(26, 10),
(26, 11),
(26, 12),
(26, 13),
(26, 14);


-- TABLES
INSERT INTO tables (id, number, status) VALUES
(0, 1, 'AVAILABLE'),
(1, 2, 'AVAILABLE'),
(2, 3, 'AVAILABLE'),
(3, 4, 'AVAILABLE'),
(4, 5, 'AVAILABLE'),
(5, 6, 'AVAILABLE'),
(6, 7, 'AVAILABLE'),
(7, 8, 'AVAILABLE'),
(8, 9, 'AVAILABLE'),
(9, 10, 'AVAILABLE'),
(10, 11, 'AVAILABLE'),
(11, 12, 'AVAILABLE'),
(12, 13, 'AVAILABLE'),
(13, 14, 'AVAILABLE'),
(14, 15, 'AVAILABLE'),
(15, 16, 'AVAILABLE'),
(16, 17, 'AVAILABLE'),
(17, 18, 'AVAILABLE'),
(18, 19, 'AVAILABLE'),
(19, 20, 'AVAILABLE');


-- CUSTOMER
INSERT INTO customer (id, name, phone, loyalty_points) VALUES ('c1', 'Nguyễn Văn A', '0900000001', 100), ('c2', 'Trần Thị B', '0900000002', 200);

-- SESSION
INSERT INTO session (id, table_id, customer_id, start_time, end_time, status) VALUES ('s1', 1, 'c1', '2025-08-18T09:00:00', NULL, 'OPEN'), ('s2', 2, 'c2', '2025-08-18T09:30:00', NULL, 'OPEN'), ('s3', 1, NULL, NULL, NULL, 'EMPTY');

-- EMPLOYEE
INSERT INTO employee (id, username, password, full_name, role) VALUES ('e1', 'admin', '1', 'Quản trị viên', 'ADMIN'), ('e2', 'user1', '2', 'Nhân viên 1', 'STAFF');

-- ORDERS
INSERT INTO orders (id, session_id, amount, loyalty_redeem_points, discount_amount, created_at, total_amount, status) VALUES ('o1', 's1', 70000, NULL, NULL, '2025-08-18T10:00:00', 70000, 'ORDERED'), ('o2', 's2', 40000, NULL, NULL, '2025-08-18T10:30:00', 40000, 'ORDERED');

-- ORDER_DETAIL
INSERT INTO order_detail (id, order_id, dish_id, quantity, note, status) VALUES
  (1, 'o1', 1, 1, 'Không hành; Ít cay', 'ORDERED'),
  (2, 'o1', 3, 2, 'Không đá', 'ORDERED'),
  (3, 'o2', 2, 1, '', 'ORDERED');

-- OPTION_TYPE
INSERT INTO option_type (id, content) VALUES
(1, 'Gia vị'),
(2, 'Đá'),
(3, 'Độ ngọt'),
(4, 'Nhiệt độ'),
(5, 'Thêm/tách');

-- OPTION_DETAIL
INSERT INTO option_detail (id, option_type_id, content) VALUES
-- Gia vị
(1, 1, 'Ít cay'),
(2, 1, 'Không hành'),
(3, 1, 'Thêm tỏi'),
(4, 1, 'Thêm ớt'),
-- Đá (cho đồ uống)
(5, 2, 'Không đá'),
(6, 2, 'Đá ít'),
(7, 2, 'Đá nhiều'),
-- Độ ngọt (cho đồ uống)
(8, 3, 'Không đường'),
(9, 3, 'Ít đường'),
(10, 3, 'Bình thường'),
(11, 3, 'Ngọt nhiều'),
-- Nhiệt độ (cho món nóng/lạnh)
(12, 4, 'Nóng'),
(13, 4, 'Ấm'),
(14, 4, 'Lạnh'),
-- Thêm/tách (cho topping, nguyên liệu)
(15, 5, 'Thêm trứng'),
(16, 5, 'Không trứng'),
(17, 5, 'Thêm phô mai'),
(18, 5, 'Không phô mai');

-- FEEDBACK
INSERT INTO feedback (id, session_id, rating, content, created_at) VALUES (1, 's1', 5, 'Rất ngon!', '2025-08-18T12:00:00'), (2, 's2', 4, 'Ổn', '2025-08-18T12:10:00');

-- PAYMENT
INSERT INTO payment (id, order_id, amount, method, status, transaction_code, paid_at, cashier_id) VALUES ('p1', 'o1', 70000, 'CASH', 'PAID', 'TXN001', '2025-08-18T12:30:00', 'e1'), ('p2', 'o2', 40000, 'CARD', 'PAID', 'TXN002', '2025-08-18T12:40:00', 'e2');

-- SERVICE_TYPE
-- SERVICE_TYPE (chỉ hỗ trợ, không gọi món hay thanh toán)
INSERT INTO service_type (id, type_name, description) VALUES
(1, 'Gọi phục vụ', 'Yêu cầu phục vụ tại bàn'),
(2, 'Gọi nước', 'Yêu cầu mang nước/đồ uống'),
(3, 'Gọi khăn/giấy', 'Yêu cầu mang khăn giấy hoặc dụng cụ'),
(4, 'Hỗ trợ kỹ thuật', 'Yêu cầu hỗ trợ kỹ thuật hoặc các vấn đề khác'),
(5, 'Dọn dẹp', 'Yêu cầu dọn dẹp bàn hoặc khu vực xung quanh');


-- SERVICE_REQUEST
INSERT INTO service_request (id, session_id, service_type_id, created_at, handle_tme, status) VALUES (1, 's1', 1, '2025-08-18T11:00:00', NULL, 'PENDING'), (2, 's2', 2, '2025-08-18T11:10:00', NULL, 'PENDING');
