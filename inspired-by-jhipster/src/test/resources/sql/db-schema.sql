CREATE TABLE JHI_USER (
  ID BIGINT PRIMARY KEY AUTO_INCREMENT,
  LOGIN VARCHAR(50) NOT NULL UNIQUE,
  PASSWORD_HASH VARCHAR NOT NULL,
  FIRST_NAME VARCHAR(50),
  LAST_NAME VARCHAR(50),
  EMAIL VARCHAR(254) UNIQUE,
  ACTIVATED BOOLEAN,
  LANG_KEY VARCHAR(10),
  IMAGE_URL VARCHAR(256),
  ACTIVATION_KEY VARCHAR(20),
  RESET_KEY VARCHAR(20),
  RESET_DATE TIMESTAMP,
  CREATED_BY VARCHAR(50) NOT NULL,
  CREATED_DATE TIMESTAMP,
  LAST_MODIFIED_BY VARCHAR(50),
  LAST_MODIFIED_DATE TIMESTAMP
);