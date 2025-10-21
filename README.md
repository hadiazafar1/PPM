# 📦 Coding Exercise – Package Management API

A RESTful backend service built with **Java 17 / Spring Boot 3**, allowing users to manage product packages, calculate total package prices, and convert them into different currencies using live exchange rates.

---

## 🚀 Features

- Create, retrieve, update, delete, and list packages  
- Real-time currency conversion (via [Frankfurter API](https://frankfurter.app))  
- Defaults to **USD** if no currency is specified  
- Supports multiple products per package  
- Uses **BigDecimal** for precise price calculations  
- Includes **Swagger UI** for easy API exploration  

---

## 🛠️ Tech Stack

| Component | Technology |
|------------|-------------|
| Language | Java 17 |
| Framework | Spring Boot 3 |
| Build Tool | Maven |
| HTTP Client | RestTemplate |
| Documentation | Swagger (springdoc-openapi) |
| Currency API | [Frankfurter.app](https://frankfurter.app) |
| Product API | [Product Service](https://product-service.herokuapp.com/api/v1/products) |

---

## ⚙️ Configuration

All configuration is in `src/main/resources/application.properties`:

```properties
server.port=8080
product.service.url=https://product-service.herokuapp.com/api/v1/
fx.service.url=https://api.frankfurter.app/latest
app.http.connect-timeout-ms=2000
app.http.read-timeout-ms=3000
```

You can override them at runtime, e.g.:

```bash
java -jar target/codingexercise-0.0.1-SNAPSHOT.jar   --server.port=8081   --fx.service.url=https://api.exchangerate.host/latest
```

---

## ▶️ How to Run Locally

### Prerequisites
- Java 17 or higher
- Maven 3.8+

### Build
```bash
mvn clean package
```

### Run
```bash
java -jar target/codingexercise-0.0.1-SNAPSHOT.jar
```

The app will start on:  
👉 **http://localhost:8080**

---

## 🌐 API Documentation (Swagger)

After starting the app:

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 📘 Example Endpoints

| Method | Endpoint | Description |
|--------|-----------|-------------|
| `POST` | `/packages` | Create a package |
| `GET` | `/packages/{id}` | Get a package by ID |
| `PUT` | `/packages/{id}` | Update a package |
| `DELETE` | `/packages/{id}` | Delete a package |
| `GET` | `/packages` | List all packages |
| `POST` | `/packages/price?currency=GBP` | Calculate package price in given currency |

### Example Request
```bash
POST /packages/price?currency=EUR
Content-Type: application/json

{
  "id": "pkg-1",
  "name": "Starter Package",
  "description": "Basic starter set",
  "productIds": ["1", "2"]
}
```

### Example Response
```json
{
  "id": "pkg-1",
  "name": "Starter Package",
  "price": 29.99,
  "currency": "EUR",
  "productIds": ["1", "2"],
  "description": "Basic starter set"
}
```

---

## 🧪 Testing

Run all tests:
```bash
mvn test
```

---


## 🐳 (Optional) Run in Docker

Build the image:
```bash
docker build -t codingexercise .
```

Run the container:
```bash
docker run -p 8080:8080 codingexercise
```

---

## 🧾 Notes

- Exchange rates fetched via [Frankfurter.app](https://frankfurter.app)  
- Product data comes from [Product Service API](https://product-service.herokuapp.com/api/v1/products)  
- Uses **BigDecimal** for financial precision (no floating-point rounding issues)

---

## 👨‍💻 Author

**Hadia Zafar**  
Built as part of a coding exercise demonstrating clean, production-style Spring Boot development with clear documentation and solid architecture.
