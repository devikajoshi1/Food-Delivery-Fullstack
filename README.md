# Food Delivery

A full-stack food delivery app.

- `backend/`: Spring Boot 4, Java 21, MySQL, Flyway
- `frontend/`: React (coming soon)

## Running the backend locally

Create `backend/src/main/resources/secrets.properties` (it is git-ignored):

```properties
DB_PASSWORD=your_mysql_password
```

Then run `./mvnw spring-boot:run` from `backend/`.
