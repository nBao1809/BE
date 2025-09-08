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
DELETE FROM dish;
DELETE FROM category;
DELETE FROM option_detail;
DELETE FROM option_type;

-- MOCKUP DATA
-- CATEGORY
INSERT INTO category (id, name) VALUES (1, 'Món chính'), (2, 'Đồ uống');

-- DISH
INSERT INTO dish (id, name, price, category_id) VALUES
  (1, 'Cơm gà', 35000, 1),
  (2, 'Phở bò', 40000, 1),
  (3, 'Trà đá', 5000, 2);

-- TABLES
INSERT INTO tables (id, number, status) VALUES (1, 1, 'AVAILABLE'), (2, 2, 'OCCUPIED');

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
INSERT INTO option_type (id, content) VALUES (1, 'Gia vị'), (2, 'Đá');

-- OPTION_DETAIL
INSERT INTO option_detail (id, option_type_id, content) VALUES (1, 1, 'Ít cay'), (2, 1, 'Không hành'), (3, 2, 'Không đá');

-- FEEDBACK
INSERT INTO feedback (id, session_id, rating, content, created_at) VALUES (1, 's1', 5, 'Rất ngon!', '2025-08-18T12:00:00'), (2, 's2', 4, 'Ổn', '2025-08-18T12:10:00');

-- PAYMENT
INSERT INTO payment (id, order_id, amount, method, status, transaction_code, paid_at, cashier_id) VALUES ('p1', 'o1', 70000, 'CASH', 'PAID', 'TXN001', '2025-08-18T12:30:00', 'e1'), ('p2', 'o2', 40000, 'CARD', 'PAID', 'TXN002', '2025-08-18T12:40:00', 'e2');

-- SERVICE_TYPE
INSERT INTO service_type (id, type_name, description) VALUES (1, 'Gọi phục vụ', 'Yêu cầu phục vụ tại bàn'), (2, 'Thanh toán', 'Yêu cầu thanh toán');

-- SERVICE_REQUEST
INSERT INTO service_request (id, session_id, service_type_id, created_at, handle_tme, status) VALUES (1, 's1', 1, '2025-08-18T11:00:00', NULL, 'PENDING'), (2, 's2', 2, '2025-08-18T11:10:00', NULL, 'PENDING');
