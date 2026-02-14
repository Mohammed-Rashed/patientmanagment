CREATE TABLE appointments (
          id BIGINT NOT NULL AUTO_INCREMENT,

          patient_id BIGINT NOT NULL,
          doctor_id  BIGINT NOT NULL,

          start_time DATETIME(3) NOT NULL,
          end_time   DATETIME(3) NOT NULL,

          status     VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
          notes      VARCHAR(500) NULL,

          created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
          updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

          PRIMARY KEY (id),

          CONSTRAINT fk_appointments_patient
              FOREIGN KEY (patient_id) REFERENCES patients(id)
                  ON DELETE RESTRICT ON UPDATE CASCADE,

          CONSTRAINT fk_appointments_doctor
              FOREIGN KEY (doctor_id) REFERENCES doctors(id)
                  ON DELETE RESTRICT ON UPDATE CASCADE,

          KEY idx_appointments_patient_time (patient_id, start_time),
          KEY idx_appointments_doctor_time  (doctor_id, start_time),
          KEY idx_appointments_status (status)
);
