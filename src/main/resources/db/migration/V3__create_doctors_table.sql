CREATE TABLE doctors (
      id BIGINT NOT NULL AUTO_INCREMENT,
      first_name VARCHAR(100) NOT NULL,
      last_name  VARCHAR(100) NOT NULL,
      email      VARCHAR(150) NULL,
      phone      VARCHAR(30)  NULL,
      specialty  VARCHAR(60)  NOT NULL,
      status     VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
      created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      PRIMARY KEY (id),
      UNIQUE KEY uk_doctors_email (email),
      UNIQUE KEY uk_doctors_phone (phone),
      KEY idx_doctors_specialty (specialty),
      KEY idx_doctors_status (status)
);