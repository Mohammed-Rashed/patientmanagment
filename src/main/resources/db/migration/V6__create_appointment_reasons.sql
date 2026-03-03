CREATE TABLE appointment_reasons (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       reason VARCHAR(255) NOT NULL ,
                       appointment_id BIGINT NOT NULL,
                       created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       PRIMARY KEY (id),
                       UNIQUE KEY uk_app_reasons_app_id (appointment_id)

);