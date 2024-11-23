CREATE TABLE Products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description VARCHAR(100),
    category_id INT, -- 카테고리 ID
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    manufacturer_id INT, -- 제조사 ID
    FOREIGN KEY (category_id) REFERENCES Categories(category_id) ON DELETE SET NULL, -- 카테고리 삭제 시 NULL 처리
    FOREIGN KEY (manufacturer_id) REFERENCES Manufacturers(manufacturer_id) ON DELETE SET NULL -- 제조사 삭제 시 NULL 처리
);

CREATE TABLE Categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE, -- 카테고리 이름
    description VARCHAR(100) -- 카테고리 설명
);

-- Manufacturers 테이블
CREATE TABLE Manufacturers (
    manufacturer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    location VARCHAR(20),
    contact VARCHAR(50) CHECK (
        contact REGEXP '^010-[0-9]{3,4}-[0-9]{4}$' OR
        contact REGEXP '^(02|0[3-6][1-4]|05[1-5]|06[1-4])-[0-9]{3}-[0-9]{4}$'
    )
);

-- Warehouse 테이블
CREATE TABLE Warehouses (
    warehouse_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    location VARCHAR(25) NOT NULL, 
    capacity INT NOT NULL CHECK (capacity >= 0) -- 창고 용량은 음수가 될 수 없음
);

-- Branch 테이블
CREATE TABLE Branches (
    branch_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(255) NOT NULL -- 
);

-- 창고 재고 관리 테이블
CREATE TABLE Warehouse_Inventory (
    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
    warehouse_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity >= 0), -- 재고 수량은 음수가 될 수 없음
    last_updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES Warehouses(warehouse_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE SET NULL -- 제품 삭제 시 NULL 처리
);

-- 주문 테이블
CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    warehouse_id INT NOT NULL, -- 배송될 창고
    branch_id INT, -- 주문 발생 지점(시점X) (NULL 허용)
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'completed', 'cancelled') NOT NULL,
    FOREIGN KEY (warehouse_id) REFERENCES Warehouses(warehouse_id) ON DELETE CASCADE,
    FOREIGN KEY (branch_id) REFERENCES Branches(branch_id) ON DELETE SET NULL -- 지점 삭제 시 주문 기록 보존
);

-- Suppliers 테이블
CREATE TABLE Suppliers (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact VARCHAR(50) CHECK (
        contact REGEXP '^010-[0-9]{3,4}-[0-9]{4}$' OR
        contact REGEXP '^(02|0[3-6][1-4]|05[1-5]|06[1-4])-[0-9]{3}-[0-9]{4}$'
    ),
    location VARCHAR(255)
);


-- 입고 기록 테이블
CREATE TABLE Incoming (
    incoming_id INT AUTO_INCREMENT PRIMARY KEY,
    warehouse_id INT NOT NULL,
    product_id INT NOT NULL,
    supplier_id INT,
    quantity INT NOT NULL CHECK (quantity > 0), -- 입고 수량은 0보다 커야 함
    incoming_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES Warehouses(warehouse_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE,
    FOREIGN KEY (supplier_id) REFERENCES Suppliers(supplier_id) ON DELETE SET NULL
);

-- 출고 기록 테이블
CREATE TABLE Outgoing (
    outgoing_id INT AUTO_INCREMENT PRIMARY KEY,
    warehouse_id INT NOT NULL,
    product_id INT NOT NULL,
    order_id INT, -- 주문 ID와 연결
    quantity INT NOT NULL CHECK (quantity > 0), -- 출고 수량은 0보다 커야 함
    outgoing_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES Warehouses(warehouse_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE SET NULL
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE SET NULL
);

-- 인덱스 추가 (성능 최적화)
CREATE INDEX idx_orders_status ON Orders(status); -- 주문 상태에 대한 빠른 검색 지원

INSERT INTO Categories (name, description) VALUES
('Electronics', 'Electronic devices'),
('Furniture', 'Household furniture'),
('Clothing', 'Apparel and accessories');

INSERT INTO Manufacturers (name, location, contact) VALUES
('Samsung', 'Seoul', '010-1234-5678'),
('IKEA', 'Gwangju', '02-987-6543'),
('Nike', 'Busan', '051-432-1234');

INSERT INTO Products (name, description, category_id, price, manufacturer_id) VALUES
('Smartphone', 'High-end phone', 1, 999.99, 1),
('Sofa', 'Comfortable sofa', 2, 499.99, 2),
('Running Shoes', 'Lightweight shoes', 3, 119.99, 3);

INSERT INTO Warehouses (name, location, capacity) VALUES
('Central Warehouse', 'Seoul', 1000),
('East Warehouse', 'Busan', 500);

INSERT INTO Branches (name, location) VALUES
('Seoul Branch', 'Seoul'),
('Busan Branch', 'Busan');

INSERT INTO Warehouse_Inventory (warehouse_id, product_id, quantity) VALUES
(1, 1, 100),
(1, 2, 50),
(2, 3, 200);

ALTER TABLE Suppliers DROP CHECK suppliers_chk_1;
-- 제약 조건 제거, 자바 코드에서 제약을 거는것이 더 괜찮을것 같습니다.

INSERT INTO Suppliers (name, contact, location) VALUES
('LG Electronics', '02-3456-7890', 'Incheon'),
('Samsung Materials', '010-5555-6666', 'Seoul');

INSERT INTO Incoming (warehouse_id, product_id, supplier_id, quantity) VALUES
(1, 1, 1, 20),
(2, 3, 2, 50);

INSERT INTO Orders (warehouse_id, branch_id, status) VALUES
(1, 1, 'pending'),
(2, 2, 'completed');

INSERT INTO Outgoing (warehouse_id, product_id, order_id, quantity) VALUES
(1, 1, 1, 10),
(2, 3, 2, 20);

SELECT * FROM Products;

SELECT   -- 특정 창고 재고 조회 쿼리
    w.name AS warehouse_name,
    p.name AS product_name,
    wi.quantity
FROM 
    Warehouse_Inventory wi
JOIN 
    Warehouses w ON wi.warehouse_id = w.warehouse_id
JOIN 
    Products p ON wi.product_id = p.product_id;

SELECT  -- 주문 및 관련 정보 조회
    o.order_id,
    w.name AS warehouse_name,
    b.name AS branch_name,
    o.status,
    o.order_date
FROM 
    Orders o
JOIN 
    Warehouses w ON o.warehouse_id = w.warehouse_id
LEFT JOIN 
    Branches b ON o.branch_id = b.branch_id;

-- 제품 가격 수정
UPDATE Products
SET price = 899.99
WHERE name = 'Smartphone';

-- 창고 재고 수정
UPDATE Warehouse_Inventory
SET quantity = quantity - 10
WHERE warehouse_id = 1 AND product_id = 1;

-- 주문 상태 변경
UPDATE Orders
SET status = 'completed'
WHERE order_id = 1;

-- 특정 주문 삭제
DELETE FROM Orders
WHERE order_id = 2;

-- 특정 제품 삭제
-- 제품 삭제시 연관된 재고와 출고 기록도 삭제되어 외래키 규칙을 변경하여야 할 것 같습니다
-- 위쪽 쿼리를 수정해 두었으나 향후 자바개발에 따라 추가 수정이 필요할수도 있습니다
DELETE FROM Products
WHERE product_id = 2;

-- 주문 상태 확인
SELECT * FROM Orders;

-- 재고 현황 확인
SELECT * FROM Warehouse_Inventory;

-- 창고별 제품명, 제품번호, 재고량
SELECT 
    w.warehouse_id AS warehouse_number, 
    w.name AS warehouse_name, 
    p.product_id AS product_number, 
    p.name AS product_name, 
    wi.quantity AS product_quantity
FROM 
    Warehouse_Inventory wi
JOIN 
    Warehouses w ON wi.warehouse_id = w.warehouse_id
JOIN 
    Products p ON wi.product_id = p.product_id;

-- 특정 창고의 제품 정보만 조회
SELECT 
    w.warehouse_id AS warehouse_number, 
    w.name AS warehouse_name, 
    p.product_id AS product_number, 
    p.name AS product_name, 
    wi.quantity AS product_quantity
FROM 
    Warehouse_Inventory wi
JOIN 
    Warehouses w ON wi.warehouse_id = w.warehouse_id
JOIN 
    Products p ON wi.product_id = p.product_id
WHERE 
    w.warehouse_id = 1; -- 창고 번호 1만 조회
