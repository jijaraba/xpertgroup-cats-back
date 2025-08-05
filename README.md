# Cats API - Spring Boot Backend

A comprehensive Spring Boot application that provides a RESTful API for cat breeds, images, and user management. This project connects to [TheCatAPI](https://thecatapi.com/) to provide rich cat breed information and integrates with database for user management.

## 🚀 Features

### Cat Breeds
- **GET `/breeds`** - Retrieve all cat breeds
- **GET `/breeds/{breed_id}`** - Get specific breed information
- **GET `/breeds/search?q={query}`** - Search breeds by name

### Images  
- **GET `/imagesbybreedid?breed_id={id}&limit={number}`** - Get images by breed ID

### User Management
- **GET `/login?username={user}&password={pass}`** - User authentication
- **GET `/register?username={user}&password={pass}&email={email}&fullName={name}`** - User registration

## 🏗️ Architecture

Built following **Clean Architecture** and **SOLID** principles:

- **Controllers**: REST endpoints and request handling
- **Services**: Business logic layer
- **Repositories**: Data access layer
- **DTOs**: Data transfer objects for API communication
- **Entities**: JPA entities for database mapping
- **Global Exception Handling**: Centralized error management

## 🛠️ Technology Stack

- **Java 24** - Programming language
- **Spring Boot 3.5.4** - Application framework
- **Spring Web** - RESTful web services
- **Spring Data JPA** - Database abstraction
- **Spring WebFlux (WebClient)** - HTTP client for external APIs
- **Spring Security Crypto** - Password encryption
- **H2 Database** - In-memory database (development)
- **PostgreSQL** - Production database (Docker setup)
- **JUnit 5** - Unit testing
- **Mockito** - Mocking framework
- **Docker & Docker Compose** - Containerization

## 🚀 Quick Start

### Prerequisites
- Java 24 or higher
- Gradle 7.0+
- Docker (optional, for containerized deployment)

### Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/xpertgroup_cats.git
   cd xpertgroup_cats
   ```

2. **Set up environment variables (for Docker)**
   ```bash
   cp .env.example .env
   # Edit .env with your TheCatAPI key from https://thecatapi.com/
   ```

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

3. **Access the API**
   - Base URL: `http://localhost:8080`
   - H2 Console: `http://localhost:8080/h2-console`

### Docker Deployment

See [DOCKER.md](DOCKER.md) for complete Docker setup instructions.

**Quick start with Docker:**
```bash
# Simple setup (H2 database)
docker-compose up --build

# Production setup (PostgreSQL)
docker-compose -f docker-compose.postgres.yml up --build
```

### Cloud Deployment

**Deploy to Render.com:**

See [RENDER.md](RENDER.md) for complete Render deployment instructions.

**Quick deploy to Render:**
1. Push code to GitHub
2. Create Render account and connect repository
3. Render auto-detects `render.yaml` and deploys
4. Add your `CATS_API_KEY` in environment variables
5. Access your API at `https://your-app.onrender.com`

## 🧪 Testing

Run the comprehensive test suite:

```bash
# Run all tests
./gradlew test

# Run with coverage
./gradlew test jacocoTestReport
```

**Test Coverage:**
- Unit tests for all services
- Integration tests for all controllers
- Exception handling tests
- External API integration tests

## 📝 API Documentation

### Example Requests

```bash
# Get all cat breeds
curl http://localhost:8080/breeds

# Get specific breed
curl http://localhost:8080/breeds/abys

# Search breeds
curl "http://localhost:8080/breeds/search?q=persian"

# Get images by breed
curl "http://localhost:8080/imagesbybreedid?breed_id=abys&limit=5"

# Register user
curl "http://localhost:8080/register?username=johndoe&password=secret123&email=john@example.com&fullName=John%20Doe"

# Login user
curl "http://localhost:8080/login?username=johndoe&password=secret123"
```

## ⚙️ Configuration

### Application Properties

Key configuration in `src/main/resources/application.properties`:

```properties
# Cat API Configuration
cats.api.base-url=https://api.thecatapi.com/v1
cats.api.key=your_api_key_here

# Database Configuration (H2)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password
```

### Environment Variables

**Create a `.env` file for Docker deployment:**
```bash
cp .env.example .env
```

**Required variables in `.env`:**
- `CATS_API_KEY` - TheCatAPI key (get free at https://thecatapi.com/)
- `CATS_API_BASE_URL` - API base URL (https://api.thecatapi.com/v1)

**Additional PostgreSQL variables (handled automatically):**
- `SPRING_DATASOURCE_URL` - Database connection
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password

## 🐛 Error Handling

The API provides consistent error responses:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Cat breed not found with ID: invalid_id",
  "timestamp": "2025-01-05T10:30:00.000Z"
}
```

**HTTP Status Codes:**
- `200` - Success
- `400` - Bad Request (validation errors)
- `401` - Unauthorized (invalid credentials)
- `404` - Not Found (resource not found)
- `409` - Conflict (user already exists)
- `500` - Internal Server Error

## 🔒 Security

- Password encryption using BCrypt
- Input validation on all endpoints
- SQL injection prevention via JPA
- Proper error handling without information disclosure

## 📋 Project Structure

```
src/
├── main/
│   ├── java/com/xpertgroup/cats/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Data transfer objects
│   │   ├── entity/         # JPA entities
│   │   ├── exception/      # Exception handling
│   │   ├── repository/     # Data repositories
│   │   └── service/        # Business logic
│   └── resources/
│       └── application.properties
└── test/                   # Unit and integration tests
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📄 License

This project is developed for educational purposes as part of the XpertGroup training program.

## 🔗 External APIs

- **TheCatAPI**: https://thecatapi.com/ - Provides cat breed information and images

## 📞 Support

For questions or support, please contact:
- Email: jijaraba@hotmail.com
- Project: XpertGroup Training Program

---

**Made with ❤️ using Spring Boot and Clean Architecture principles**