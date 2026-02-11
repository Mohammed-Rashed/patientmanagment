CREATE TABLE patients (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          first_name VARCHAR(100) NOT NULL,
                          last_name  VARCHAR(100) NOT NULL,
                          email      VARCHAR(150) NOT NULL,
                          phone      VARCHAR(30) NULL,
                          date_of_birth DATE NULL,
                          status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                          created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          PRIMARY KEY (id),
                          UNIQUE KEY uk_patients_email (email),
                          UNIQUE KEY uk_patients_phone (phone),
                          KEY idx_patients_status (status)
);
