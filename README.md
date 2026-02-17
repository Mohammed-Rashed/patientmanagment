# Patient Management (Spring Boot + MySQL + Flyway)

A learning-focused Spring Boot project that implements a simple clinic workflow:
- Manage **Patients**
- Manage **Doctors**
- Manage **Appointments** (create, list, update)
- Generate **Available Slots** for a doctor (next 2 days, fixed window, 15-minute slots)
- Appointment **Cancel** flow (soft-cancel using status)


---

## Tech Stack

- Java + Spring Boot
- Maven
- MySQL
- Spring Data JPA (Hibernate)
- Flyway (DB migrations)
- Bean Validation 
- Lombok

---

##  Features

### Patients
- CRUD
- Validation + custom error responses

### Doctors
- CRUD
- `DoctorSpecialty` 

### Appointments
- Create / Update / List
- Validations:
  - must be in the future
  - `endTime` must be after `startTime`
  - minimum duration (example: 15 minutes)
  - maximum duration (example: 2 hours)
- Overlap prevention (doctor cannot be double-booked)
- Cancel endpoint (status changes to `CANCELED`)

### Availability (Available Slots)
- Rules:
  - Next **2 days** only
  - Working hours fixed (example): **4:00 PM → 10:00 PM**
  - Slot size: **15 minutes**
  - Excludes already booked slots (ignoring `CANCELED`)
  - Excludes past slots

---

## Plan

### 1: Auth & Roles
- JWT auth (login/register)
- Roles: ADMIN / DOCTOR / PATIENT
- Link `users` to `doctors` and `patients`

### Phase 1 Portals (API-first)
- Doctor portal:
  - mark appointments as `COMPLETED`
  - dashboard summary (today/upcoming/completed/canceled)
- Admin portal:
  - manage everything
  - define working days/hours per doctor
- Patient portal:
  - book from available slots
  - view profile + appointments

### Phase 2: Messaging (Kafka or RabbitMQ)
- Publish events:
  - AppointmentCreated / AppointmentCanceled / AppointmentCompleted
- Outbox pattern for reliability
- Notification/Audit/Analytics consumers

---

## Getting Started

### Prerequisites
- Java (your project currently uses modern JDK)
- Maven
- MySQL running locally

### Configuration
Update `src/main/resources/application.properties`:

```properties
spring.application.name=patientmanagment
server.port=8082

spring.datasource.url=jdbc:mysql://localhost:3306/patientdb?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.jdbc.time_zone=UTC

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.fail-on-missing-locations=true

spring.web.error.include-message=always
spring.web.error.include-binding-errors=always
spring.web.error.include-stacktrace=never
