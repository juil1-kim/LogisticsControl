-- 1. Categories 테이블
CREATE TABLE Categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE, -- 카테고리 이름
    description VARCHAR(100) -- 카테고리 설명
);

-- 2. Manufacturers 테이블
CREATE TABLE Manufacturers (
    manufacturer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    location VARCHAR(20),
    contact VARCHAR(50) CHECK (
        contact REGEXP '^010-[0-9]{3,4}-[0-9]{4}$' OR
        contact REGEXP '^(02|0[3-6][1-4]|05[1-5]|06[1-4])-[0-9]{3}-[0-9]{4}$'
    )
);

-- 3. Suppliers 테이블
CREATE TABLE Suppliers (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact VARCHAR(50) CHECK (
        contact REGEXP '^010-[0-9]{3,4}-[0-9]{4}$' OR
        contact REGEXP '^(02|0[3-6][1-4]|05[1-5]|06[1-4])-[0-9]{3}-[0-9]{4}$'
    ),
    location VARCHAR(255)
);

-- 4. Warehouses 테이블
CREATE TABLE Warehouses (
    warehouse_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    location VARCHAR(25) NOT NULL, 
    capacity INT NOT NULL CHECK (capacity >= 0) -- 창고 용량은 음수가 될 수 없음
);

-- 5. Branches 테이블
CREATE TABLE Branches (
    branch_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(255) NOT NULL
);

-- 6. Administrators 테이블
CREATE TABLE Administrators (
    admin_id INT AUTO_INCREMENT PRIMARY KEY, -- 관리자 ID (고유)
    user_id VARCHAR(50) UNIQUE NOT NULL, -- 로그인용 사용자 ID (중복 불가)
    password VARCHAR(255) NOT NULL, -- 로그인용 비밀번호 (암호화 필요) --> 주일님 설계
    role ENUM('root', 'general') NOT NULL, -- 관리자 역할 (루트 관리자, 일반 관리자)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 생성일
);

-- 7. Products 테이블
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

-- 8. Administrator_Warehouses 테이블
CREATE TABLE Administrator_Warehouses (
    admin_warehouse_id INT AUTO_INCREMENT PRIMARY KEY, -- 관계 ID
    admin_id INT NOT NULL, -- 관리자 ID
    warehouse_id INT NOT NULL, -- 창고 ID
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 할당 시간
    FOREIGN KEY (admin_id) REFERENCES Administrators(admin_id) ON DELETE CASCADE, -- 관리자가 삭제되면 관련 데이터 삭제
    FOREIGN KEY (warehouse_id) REFERENCES Warehouses(warehouse_id) ON DELETE CASCADE -- 창고 삭제 시 관련 데이터 삭제
);

-- 9. Warehouse_Inventory 테이블
CREATE TABLE Warehouse_Inventory (
    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
    warehouse_id INT NOT NULL,
    product_id INT, -- NULL 가능으로 변경
    quantity INT NOT NULL CHECK (quantity >= 0), -- 재고 수량은 음수가 될 수 없음
    last_updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES Warehouses(warehouse_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE SET NULL -- 제품 삭제 시 NULL 처리
);


-- 10. Orders 테이블
CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    warehouse_id INT NOT NULL, -- 배송될 창고
    branch_id INT, -- 주문 발생 지점 (NULL 허용)
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('대기', '완료', '취소') NOT NULL,
    FOREIGN KEY (warehouse_id) REFERENCES Warehouses(warehouse_id) ON DELETE CASCADE,
    FOREIGN KEY (branch_id) REFERENCES Branches(branch_id) ON DELETE SET NULL -- 지점 삭제 시 주문 기록 보존
);

-- 11. Supplier_Products 테이블
CREATE TABLE Supplier_Products (
    supplier_product_id INT AUTO_INCREMENT PRIMARY KEY, -- PK
    supplier_id INT NOT NULL, -- 공급자 ID FK
    product_id INT NOT NULL, -- 제품 ID FK
    UNIQUE(supplier_id, product_id), -- 중복 방지: 한 공급자-제품 조합은 한 번만 저장
    FOREIGN KEY (supplier_id) REFERENCES Suppliers(supplier_id) ON DELETE CASCADE, -- 공급자 삭제 시 관련 데이터 삭제
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE -- 제품 삭제 시 관련 데이터 삭제
);

-- 12. Incoming 테이블
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

-- 13. Outgoing 테이블
CREATE TABLE Outgoing (
    outgoing_id INT AUTO_INCREMENT PRIMARY KEY,
    warehouse_id INT NOT NULL,
    product_id INT, -- NULL 가능으로 변경
    order_id INT, -- 주문 ID와 연결
    quantity INT NOT NULL CHECK (quantity > 0), -- 출고 수량은 0보다 커야 함
    outgoing_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES Warehouses(warehouse_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE SET NULL, -- 제품 삭제 시 NULL 처리
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE SET NULL
);

INSERT INTO Categories (name, description) VALUES
('전자제품', '전자기기 관련 제품'),
('생활용품', '일상생활에 필요한 용품'),
('가구', '가구 및 인테리어 제품'),
('의류', '패션 및 의류 관련 제품'),
('서적', '책 및 문구류');

INSERT INTO Manufacturers (name, location, contact) VALUES
('삼성전자', '서울', '010-1234-5678'),
('LG전자', '서울', '02-987-6543'),
('한샘', '경기', '031-876-5432'),
('현대오토에버', '서울', '02-456-7890'),
('지엔씨', '부산', '051-654-9876');

INSERT INTO Suppliers (name, contact, location) VALUES
('삼성물산', '010-1111-2222', '서울'),
('주일무역', '02-123-4567', '부산'),
('가연물류', '032-444-5555', '인천'),
('대우상사', '010-3333-4444', '광주'),
('민호무역', '010-5555-6666', '대전');

INSERT INTO Warehouses (name, location, capacity) VALUES
('서울창고', '서울', 1000),
('부산창고', '부산', 2000),
('인천창고', '인천', 1500),
('제3보급창고', '광주', 1800),
('A보급창고', '대전', 1200);

INSERT INTO Administrators (user_id, password, role) VALUES
('admin1', 'password1', 'root'),
('admin2', 'password2', 'general'),
('admin3', 'password3', 'general'),
('admin4', 'password4', 'general'),
('admin5', 'password5', 'root');

INSERT INTO Products (name, description, category_id, price, manufacturer_id) VALUES
('스마트폰', '최신 스마트폰', 1, 1000000, 1),
('세탁기', '고효율 세탁기', 1, 500000, 2),
('이케아 소파', '3인용 소파', 3, 300000, 3),
('칼하트 티셔츠', '면 소재 티셔츠', 4, 120000, 4),
('문구세트', '종합 문구세트', 5, 15000, 5);

INSERT INTO Administrator_Warehouses (admin_id, warehouse_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

INSERT INTO Warehouse_Inventory (warehouse_id, product_id, quantity) VALUES
(1, 1, 100),
(2, 2, 50),
(3, 3, 20),
(4, 4, 30),
(5, 5, 40);

INSERT INTO Branches (name, location) VALUES
('서울중앙지점', '서울특별시 강남구 삼성동'),
('부산서부지점', '부산광역시 사상구 괘법동'),
('창원중부지점', '창원특례시 성산구 성주동'),
('광주남부지점', '광주광역시 남구 봉선동'),
('대전북부지점', '대전광역시 대덕구 오정동');

INSERT INTO Orders (warehouse_id, branch_id, status) VALUES
(1, 1, '대기'),
(2, 2, '완료'),
(3, 3, '취소'),
(4, 4, '대기'),
(5, 5, '완료');

INSERT INTO Supplier_Products (supplier_id, product_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

INSERT INTO Incoming (warehouse_id, product_id, supplier_id, quantity) VALUES
(1, 1, 1, 50),
(2, 2, 2, 30),
(3, 3, 3, 10),
(4, 4, 4, 20),
(5, 5, 5, 15);

INSERT INTO Outgoing (warehouse_id, product_id, order_id, quantity) VALUES
(1, 1, 1, 10),
(2, 2, 2, 5),
(3, 3, 3, 2),
(4, 4, 4, 8),
(5, 5, 5, 6);

