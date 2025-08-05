# Docker Setup for Cats API

This project includes Docker configurations for easy deployment and development.

## Quick Start

### Option 1: Using H2 Database (Simple Setup)

Run the application with in-memory H2 database:

```bash
docker-compose up --build
```

The application will be available at: http://localhost:8080

### Option 2: Using PostgreSQL Database (Production-like Setup)

If you need PostgreSQL for database persistence:

```bash
docker-compose -f docker-compose.postgres.yml up --build
```

This will start:
- PostgreSQL database on port 5432
- Cats API application on port 8080

## Available Endpoints

Once running, you can test the following endpoints:

### Cat Breeds
- `GET http://localhost:8080/breeds` - Get all cat breeds
- `GET http://localhost:8080/breeds/{breed_id}` - Get specific breed (e.g., `/breeds/abys`)
- `GET http://localhost:8080/breeds/search?q={query}` - Search breeds (e.g., `/breeds/search?q=persian`)

### Images
- `GET http://localhost:8080/imagesbybreedid?breedId={breed_id}&limit={number}` - Get images by breed

### Users
- `GET http://localhost:8080/login?username={user}&password={pass}` - User login
- `GET http://localhost:8080/register?username={user}&password={pass}&email={email}&fullName={name}` - User registration

## Docker Commands

### Build only
```bash
docker-compose build
```

### Run in background
```bash
docker-compose up -d
```

### View logs
```bash
docker-compose logs -f cats-app
```

### Stop services
```bash
docker-compose down
```

### Stop and remove volumes (PostgreSQL setup)
```bash
docker-compose -f docker-compose.postgres.yml down -v
```

## Environment Variables

The application uses these environment variables in Docker:

- `CATS_API_BASE_URL`: Base URL for TheCatAPI (default: https://api.thecatapi.com/v1)
- `CATS_API_KEY`: API key for TheCatAPI
- `SPRING_PROFILES_ACTIVE`: Spring profile (set to 'docker')

For PostgreSQL setup, additional variables:
- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password

## Database Access

### H2 Console (H2 setup only)
When using H2, you can access the database console at:
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

### PostgreSQL (PostgreSQL setup only)
Connect to PostgreSQL:
- Host: localhost
- Port: 5432
- Database: catsdb
- Username: cats_user
- Password: cats_password

## Notes

- The application will automatically create the database schema on startup
- External API calls to TheCatAPI will work from within the container
- All unit tests run during the Docker build process
- The application uses Java 24 runtime