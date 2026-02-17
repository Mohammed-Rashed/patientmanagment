CREATE TABLE roles (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       name VARCHAR(50) NOT NULL UNIQUE,
                       created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       PRIMARY KEY (id)
);

CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       name VARCHAR(120) NULL,
                       email VARCHAR(120) NULL,
                       phone VARCHAR(25) NULL,
                       password VARCHAR(255) NOT NULL,
                       enabled TINYINT(1) NOT NULL DEFAULT 1,
                       created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       PRIMARY KEY (id),
                       UNIQUE KEY uk_users_email (email),
                       UNIQUE KEY uk_users_phone (phone)
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT
);

-- seed roles
INSERT INTO roles (name) VALUES ('ADMIN'), ('DOCTOR'), ('PATIENT');

-- link doctor/patient to user (nullable now)
ALTER TABLE doctors
    ADD COLUMN user_id BIGINT NULL,
    ADD UNIQUE KEY uk_doctors_user_id (user_id),
    ADD CONSTRAINT fk_doctors_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL;

ALTER TABLE patients
    ADD COLUMN user_id BIGINT NULL,
    ADD UNIQUE KEY uk_patients_user_id (user_id),
    ADD CONSTRAINT fk_patients_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL;
