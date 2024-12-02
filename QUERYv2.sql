CREATE TABLE Products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category_id INT,
    price DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    manufacturer_id INT,
    FOREIGN KEY (category_id) REFERENCES Categories(category_id) ON DELETE SET NULL,
    FOREIGN KEY (manufacturer_id) REFERENCES Manufacturers(manufacturer_id)
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

-- 관리자 테이블
CREATE TABLE Administrators (
    admin_id INT AUTO_INCREMENT PRIMARY KEY, -- 관리자 ID (고유)
    user_id VARCHAR(50) UNIQUE NOT NULL, -- 로그인용 사용자 ID (중복 불가)
    password VARCHAR(255) NOT NULL, -- 로그인용 비밀번호 (암호화 필요)
    role ENUM('root', 'general') NOT NULL, -- 관리자 역할 (루트 관리자, 일반 관리자)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 생성일
);

-- 관리자와 창고의 관계를 나타내는 테이블
CREATE TABLE Administrator_Warehouses (
    admin_warehouse_id INT AUTO_INCREMENT PRIMARY KEY, -- 관계 ID
    admin_id INT NOT NULL, -- 관리자 ID
    warehouse_id INT NOT NULL, -- 창고 ID
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 할당 시간
    FOREIGN KEY (admin_id) REFERENCES Administrators(admin_id) ON DELETE CASCADE, -- 관리자가 삭제되면 관련 데이터 삭제
    FOREIGN KEY (warehouse_id) REFERENCES Warehouses(warehouse_id) ON DELETE CASCADE -- 창고 삭제 시 관련 데이터 삭제
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
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE SET NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE SET NULL
);

-- 새로 추가한 공급자 - 상품 관계테이블입니다. 사실상 중간 연결테이블.
CREATE TABLE Supplier_Products (
    supplier_product_id INT AUTO_INCREMENT PRIMARY KEY, -- PK
    supplier_id INT NOT NULL, -- 공급자 ID FK
    product_id INT NOT NULL, -- 제품 ID FK
    UNIQUE(supplier_id, product_id), -- 중복 방지: 한 공급자-제품 조합은 한 번만 저장
    FOREIGN KEY (supplier_id) REFERENCES Suppliers(supplier_id) ON DELETE CASCADE, -- 공급자 삭제 시 관련 데이터 삭제
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE -- 제품 삭제 시 관련 데이터 삭제
);


-- 인덱스 추가 (성능 최적화)
CREATE INDEX idx_orders_status ON Orders(status); -- 주문 상태에 대한 빠른 검색 지원

ALTER TABLE Suppliers DROP CHECK suppliers_chk_1;
-- 제약 조건 제거, 자바 코드에서 제약을 거는것이 더 괜찮을것 같습니다.


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

