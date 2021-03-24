CREATE TABLE CART (
  ID BIGINT PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE ITEM (
  ID BIGINT PRIMARY KEY AUTO_INCREMENT,
  CART_ID INT NOT NULL,
  FOREIGN KEY (CART_ID) REFERENCES CART(ID)
);