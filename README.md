# 📚 Library API (Back-end)

This is the back-end for the **Library API**, built with **Spring Boot**. It provides a RESTful API to manage book records and integrates with the **OpenAI API** for generating book insights.

---

## 🚀 Tech Stack
- **Spring Boot** (Java 21)
- **Spring Data JPA** (Hibernate)
- **H2 Database** (for development)
- **Swagger** (API documentation)
- **Resilience4j** (Retry & Circuit Breaker)
- **Maven** (Dependency Management)

---

## 🛠️ Prerequisites
- **JDK 21+**
- **Maven 3.8+**
- **OpenAI API Key** (for book insights)


## 🔧 Setup & Run

1. Navigate to the backend folder:
   ```bash
   cd library-service
Add your OpenAI API Key to application.yml:


openai:
  api-key: "YOUR_OPENAI_API_KEY"
  
Build and run the application:

mvn clean install

mvn spring-boot:run -Dspring-boot.run.arguments="--openai.api.key=$API_KEY"


✅ API URL: http://localhost:8080

🔗 Swagger Documentation: http://localhost:8080/swagger-ui/index.html

## 📑 API Endpoints

| Method | Endpoint                     | Description                         |
|--------|------------------------------|-------------------------------------|
| `POST` | `/api/v1/books`              | Create a new book                  |
| `GET`  | `/api/v1/books`              | Retrieve all books                 |
| `GET`  | `/api/v1/books/{id}`         | Retrieve book details by ID        |
| `GET`  | `/api/v1/books/search`       | Search books by title and author   |
| `GET`  | `/api/v1/books/{id}/ai-insights` | Get AI insights for a book    |

🛡️ Fault Tolerance (Resilience4j)
We use Retry and Circuit Breaker patterns for the OpenAI API:

Retry: Attempts up to 3 retries if the external API fails.
Circuit Breaker: Opens the circuit after 50% failure rate to prevent overload.
Configuration in application.yml:

resilience4j:
  retry:
    instances:
      openaiRetry:
        maxAttempts: 3
        waitDuration: 5s
  circuitbreaker:
    instances:
      openaiCircuitBreaker:
        failureRateThreshold: 50
        slidingWindowSize: 10
        waitDurationInOpenState: 15s
