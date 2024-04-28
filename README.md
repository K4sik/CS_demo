# Users Controller API

This is a RESTful controller for managing user-related operations. It provides endpoints for CRUD operations on user data, as well as searching for users by birthdate.

## Getting Started

To use this controller, you need to have a running Spring Boot application with the necessary dependencies and configurations.

## Endpoints

### Get All Users
- **URL**: `/api/users/`
- **Method**: `GET`
- **Description**: Retrieves all users.
- **Response**: Returns a list of all users.

### Get All Users with Pagination
- **URL**: `/api/users/all/pagination`
- **Method**: `GET`
- **Parameters**:
  - `pageNo` (optional): Page number for pagination (default: 0)
  - `pageSize` (optional): Number of items per page (default: 5)
- **Description**: Retrieves all users with pagination.
- **Response**: Returns paginated user data.

### Add User
- **URL**: `/api/users/add`
- **Method**: `POST`
- **Body**: UserDTO object representing the user to be added.
- **Description**: Adds a new user.
- **Response**: Returns a success message upon successful addition.

### Get User by ID
- **URL**: `/api/users/{userId}`
- **Method**: `GET`
- **Parameters**: `userId` - ID of the user to retrieve.
- **Description**: Retrieves a user by ID.
- **Response**: Returns the user with the specified ID.

### Update User
- **URL**: `/api/users/{userId}`
- **Method**: `PUT`
- **Parameters**: `userId` - ID of the user to update.
- **Body**: UserDTO object representing the updated user data.
- **Description**: Updates an existing user.
- **Response**: Returns a success message upon successful update.

### Delete User
- **URL**: `/api/users/{userId}`
- **Method**: `DELETE`
- **Parameters**: `userId` - ID of the user to delete.
- **Description**: Deletes a user by ID.
- **Response**: Returns a success message upon successful deletion.

### Search Users by Birthdate
- **URL**: `/api/users/search`
- **Method**: `GET`
- **Parameters**:
  - `dateFrom`: Start date for birthdate search (format: dd-MM-yyyy)
  - `dateTo`: End date for birthdate search (format: dd-MM-yyyy)
- **Description**: Searches users by birthdate within a specified range.
- **Response**: Returns users whose birthdates fall within the specified range.

## Logging
- This controller logs important events using the Log4j2 framework.

## Cross-Origin Resource Sharing (CORS)
- Cross-Origin Resource Sharing is enabled for this controller to allow requests from other domains.

## Swagger Documentation
- Swagger documentation is available for this controller to facilitate testing and understanding of the API endpoints.
- **URL**: `swagger-ui/index.html`
