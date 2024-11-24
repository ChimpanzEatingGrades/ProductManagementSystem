
CREATE TABLE Categories (

    category_id INT AUTO_INCREMENT PRIMARY KEY,

    category_name VARCHAR(50) NOT NULL

);

CREATE TABLE Suppliers (

    supplier_id INT AUTO_INCREMENT PRIMARY KEY,

    supplier_name VARCHAR(100) NOT NULL,

    contact_info VARCHAR(255)

);

CREATE TABLE Items (

    item_id INT AUTO_INCREMENT PRIMARY KEY,

    item_name VARCHAR(100) NOT NULL,

    description TEXT,

    category_id INT,

    unit_price DECIMAL(10,2) NOT NULL,

    quantity_on_hand INT NOT NULL,

    reorder_level INT,

    FOREIGN KEY (category_id) REFERENCES Categories(category_id)

);

CREATE TABLE Transactions (

    transaction_id INT AUTO_INCREMENT PRIMARY KEY,

    item_id INT,

    transaction_date DATETIME,

    quantity INT,

    transaction_type VARCHAR(20),

    notes TEXT,

    FOREIGN KEY (item_id) REFERENCES Items(item_id)

);

CREATE TABLE PurchaseOrders (

    purchase_order_id INT AUTO_INCREMENT PRIMARY KEY, 

    order_date DATETIME,

    supplier_id INT,

    total_amount DECIMAL(10,2), /* This is a calculated field, total cost of items in the order */

    FOREIGN KEY (supplier_id) REFERENCES Suppliers(supplier_id)

);

CREATE TABLE PurchaseOrderItems (

    purchase_order_item_id INT AUTO_INCREMENT PRIMARY KEY, 

    purchase_order_id INT,

    item_id INT, /* the items here are from the items table */

    quantity INT, /* number of items in the order */

    unit_price DECIMAL(10,2), /* price per item */

    FOREIGN KEY (purchase_order_id) REFERENCES PurchaseOrders(purchase_order_id),

    FOREIGN KEY (item_id) REFERENCES Items(item_id)

); 