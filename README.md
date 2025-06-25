# Recipes Application

This is a RESTful API for managing cooking recipes built with Spring Boot 3.5.0 and PostgreSQL 14.17. The application allows users to create, read, update, and delete recipes, as well as search for recipes using various filters.

## Docker Setup

This project includes Docker configuration for both the application and PostgreSQL database.

### Prerequisites

- Docker
- Docker Compose

### Running with Docker

1. Build and start the containers:

```bash
docker-compose up -d
```

This will:
- Build the Java application using the Dockerfile
- Start PostgreSQL 14.17 with a persistent volume
- Configure the application to connect to the database

2. Access the application:

The application will be available at http://localhost:8080

3. Stop the containers:

```bash
docker-compose down
```

To remove the volumes as well:

```bash
docker-compose down -v
```

## Database Configuration

The PostgreSQL database is configured with:
- Database name: recipes
- Username: postgres
- Password: root
- Port: 5432

## Development

For local development without Docker:

1. Ensure you have PostgreSQL 14.x installed and running locally
2. The application.properties file is configured to connect to a local PostgreSQL instance
3. If you want to run the code in the IDE, comment all the app configuration in the docker-compose.yml file
4. Run the application using Maven:

```bash
./mvnw spring-boot:run
```

## API Documentation

### Endpoints

#### Recipe Management

- `GET /api/recipes` - Get all recipes
- `GET /api/recipes/{id}` - Get a recipe by ID
- `POST /api/recipes` - Create a new recipe
- `PUT /api/recipes/{id}` - Update an existing recipe
- `DELETE /api/recipes/{id}` - Delete a recipe
#### Search Functionality

- `POST /api/recipes/search` - Search for recipes with filters

### Request/Response Examples

#### Create a Recipe

Request:
```
POST /api/recipes
```
```json
{
  "title": "Recipe Name",
  "description": "Brief description of the recipe",
  "ingredients": ["Ingredient 1", "Ingredient 2", "Ingredient 3", "Ingredient 4"],
  "instructions": "Step-by-step instructions for preparing the recipe",
  "vegetarian": false,
  "servings": 2
}
```

Response:
```json
{
  "id": 1,
  "title": "Recipe Name",
  "description": "Brief description of the recipe",
  "ingredients": ["Ingredient 1", "Ingredient 2", "Ingredient 3", "Ingredient 4"],
  "instructions": "Step-by step instructions for preparing the recipe",
  "vegetarian": false,
  "servings": 2,
  "createdAt": "2025-06-19T10:30:00",
  "updatedAt": "2025-06-19T10:30:00"
}
```

#### Search for Recipes

Request:
```
POST /api/recipes/search
```
```json
{
  "vegetarian": true,
  "servings": 4,
  "includeIngredients": ["Ingredient A", "Ingredient B"],
  "excludeIngredients": ["Ingredient C", "Ingredient D"],
  "instructionText": "cooking method"
}
```

Response:
```json
[
  {
    "id": 2,
    "title": "Vegetarian Recipe",
    "description": "Description of a vegetarian recipe",
    "ingredients": ["Ingredient A", "Ingredient B", "Ingredient C", "Ingredient D"],
    "instructions": "Instructions for preparing the vegetarian recipe using the cooking method",
    "vegetarian": true,
    "servings": 4,
    "createdAt": "2025-06-23T11:30:00",
    "updatedAt": "2025-06-23T11:30:00"
  }
]
```

## Design Choices

### Architecture

The application follows a layered architecture:

1. **Controller Layer**: Handles HTTP requests and responses
2. **Service Layer**: Contains business logic
3. **Repository Layer**: Manages data access
4. **Model Layer**: Defines the domain entities

### Key Design Decisions

- **Data Transfer Objects (DTOs)**: Separate DTOs for requests and responses to decouple the API from the domain model
- **Validation**: Bean validation for request data to ensure data integrity
- **Exception Handling**: Global exception handler for consistent error responses
- **Search Functionality**: Flexible search with multiple optional filters
- **Database**: Used JPA/Hibernate with PostgreSQL for data persistence
- **Testing**: Unit tests for controllers using MockMvc

### Assumptions

- Ingredients are stored as simple strings rather than as separate entities
- A recipe can be vegetarian or non-vegetarian (boolean flag)
- Servings is a positive integer
- Instructions are stored as a single text field rather than step-by-step
- Search is case-insensitive for text fields
