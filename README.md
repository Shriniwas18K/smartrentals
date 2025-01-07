# Smartrentals Operations API

## Overview

The Smartrentals Operations API allows users to perform CRUD (Create, Read, Update, Delete) operations on property listings. Authenticated users can post new properties, including base64-encoded images. All users can retrieve property listings and contact property owners for rentals. There are three public routes available without authentication: `/`,`/registration` and `/search`.

## Technologies used : 
- **Spring Boot 3.3.6**
- **Java SE 17**(open-jdk-17)
- **Spring Data JPA**
- **Lombok**
- **H2-database**
- **Spring security 6**
- **JUnit,Mockito**
- **Maven and VSCODE IDE with extensions and plugins**


## Authentication

The API uses HTTP Basic stateless authentication. Only authenticated users can create, update, and delete property listings. Unauthenticated users can retrieve property listings, contact property owners, and access the public routes.

## Entities

### Property

The `Property` entity represents a property listing with various attributes, including address, user, area, rent, type, and base64-encoded images.

#### Attributes

- **id**: Unique identifier for the property.
- **address**: Address of the property.
- **user**: Reference to the user who owns the property.
- **area**: Area of the property in square feet.
- **rent**: Rent amount for the property.
- **type**: Type of the property (ONE_BHK,TWO_BHK,THREE_BHK,BUNGALOW,ONE_RK).
- **base64EncodedImages**: List of base64-encoded images of the property.

### User

The `User` entity represents a user with various attributes, including first name, last name, phone, email, and a list of properties they own.

#### Attributes

- **id**: Unique identifier for the user.
- **firstName**: First name of the user.
- **lastName**: Last name of the user.
- **phone**: Phone number of the user.
- **email**: Email address of the user (used as username in Spring Security).
- **password**: Encrypted password of the user.
- **properties**: List of properties owned by the user.

## Endpoints

### Public Endpoints

##### 1. User Registration

**Endpoint:** `POST /registration`  
**Description:** Register a new user.  
**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "phone": "1234567890",
  "email": "johndoe@example.com",
  "password": "password123"
}
```
**Response Body:**
```json
{
  "message": "Registration Successful.",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "phone": "1234567890",
    "email": "johndoe@example.com"
  }
}
```
##### 2. Search Properties

**Endpoint:** GET /search 
**Description:** Search for properties based on criteria.
**Request Query Parameters:**

    - address (optional): Search by address.

    - type (optional): Search by property type (ONE_BHK,TWO_BHK,THREE_BHK,BUNGALOW,ONE_RK).

    - minRent (optional): Minimum rent amount.

    - maxRent (optional): Maximum rent amount.

**Response:**
```json
{
  "message": "Properties Retrieved.",
  "data": [
    {
      "id": 1,
      "address": "123 Main St, Springfield",
      "area": 1200,
      "rent": 1500,
      "type": "THREE_BHK",
      "base64EncodedImages": [
        "iVBORw0KGgoAAAANSUhEUgAAAoAAAAHgCAIAAAD7E5cJAAAABmJLR0QA/wD/AP+gvaeT..."
      ]
    },
    {
      "id": 2,
      "address": "456 Elm St, Springfield",
      "area": 900,
      "rent": 1000,
      "type": "TWO_BHK",
      "base64EncodedImages": [
        "iVBORw0KGgoAAAANSUhEUgAAAoAAAAHgCAIAAAD7E5cJAAAABmJLR0QA/wD/AP+gvaeT..."
      ]
    }
  ]
}
```
##### 3. Greetings
**Endpoint:** GET /
**Response Status:** 200 OK
**Response Body**
```json
{
    "message":"Greetings!! Have look at our docs https://github.com/Shriniwas18K/properties-crud/tree/main?tab=readme-ov-file#Smartrentals-operations-api"
}
```

### Private Endpoints : Require Httpbasic stateless authentication

#### Properties-CRUD

##### 1. Create a New Property

**URL:** /property/new
**Method:** POST
**Authentication:** HTTP Basic
**Consumes:** application/json
**Request Body:**
```json
{
  "address": "456 Elm Street, Springfield, Somewhere",
  "area": 950,
  "rent": 1200,
  "type": "TWO_BHK",
  "base64EncodedImages": [
    "iVBORw0KGgoAAAANSUhEUgAAAoAAAAHgCAIAAAD7E5cJAAAABmJLR0QA/wD/AP+gvaeT...",
    // NOTE : it is responsiblity of user to provide the base64 encoded images,
    // the rest api doesnt check wheter encoding is valid, the given images are
    // stored as it is.
  ]
}
```
**Response:**
**Status:** 201 Created
**Body:**
```json
{
  "message": "Property Listed.",
  "data": {
    "id": 1,
    "address": "456 Elm Street, Springfield, Somewhere",
    "area": 950,
    "rent": 1200,
    "type": "TWO_BHK",
    "base64EncodedImages": [
      "iVBORw0KGgoAAAANSUhEUgAAAoAAAAHgCAIAAAD7E5cJAAAABmJLR0QA/wD/AP+gvaeT..."
    ]
  }
}
```
##### 2. Get All Properties of the Logged-in User

**URL:** /property/user
**Method:** GET
**Authentication:** HTTP Basic
**Response:**
**Status:** 200 OK
**Body:**
```json
{
  "message": "Properties retrieved.",
  "count": 2,
  "data": [
    {
      "id": 1,
      "address": "456 Elm Street, Springfield, Somewhere",
      "area": 950,
      "rent": 1200,
      "type": "TWO_BHK",
      "base64EncodedImages": [
        "iVBORw0KGgoAAAANSUhEUgAAAoAAAAHgCAIAAAD7E5cJAAAABmJLR0QA/wD/AP+gvaeT..."
      ]
    },
    {
      "id": 2,
      "address": "123 Main St, Springfield",
      "area": 1200,
      "rent": 1500,
      "type": "THREE_BHK",
      "base64EncodedImages": [
        "iVBORw0KGgoAAAANSUhEUgAAAoAAAAHgCAIAAAD7E5cJAAAABmJLR0QA/wD/AP+gvaeT..."
      ]
    }
  ]
}
```
##### 3. Get a Property by ID

**URL:** /property/{id}
**Method:** GET
**Authentication:** HTTP Basic
**Response:**
**Status:** 200 OK or 404 Not Found
**Body:**
```json
{
  "message": "Property Retrieved.",
  "data": {
    "id": 1,
    "address": "456 Elm Street, Springfield, Somewhere",
    "area": 950,
    "rent": 1200,
    "type": "TWO_BHK",
    "base64EncodedImages": [
      "iVBORw0KGgoAAAANSUhEUgAAAoAAAAHgCAIAAAD7E5cJAAAABmJLR0QA/wD/AP+gvaeT..."
    ]
  }
}
```
##### 4. Update a Property

**URL:** /property/{id}
**Method:** PUT
**Authentication:** HTTP Basic
**Consumes:** application/json
**Request Body:**
```json
{
  "address": "789 Oak Street, Springfield",
  "area": 1000,
  "rent": 1300,
  "type": "TWO_BHK",
  "base64EncodedImages": [
    "iVBORw0KGgoAAAANSUhEUgAAAoAAAAHgCAIAAAD7E5cJAAAABmJLR0QA/wD/AP+gvaeT..."
  ]
}
```
**Response:**
**Status:** 200 OK or 404 Not Found
**Body:**
```json
{
  "message": "Property Updated.",
  "data": {
    "id": 1,
    "address": "789 Oak Street, Springfield",
    "area": 1000,
    "rent": 1300,
    "type": "TWO_BHK",
    "base64EncodedImages": [
      "iVBORw0KGgoAAAANSUhEUgAAAoAAAAHgCAIAAAD7E5cJAAAABmJLR0QA/wD/AP+gvaeT..."
    ]
  }
}
```
##### 5. Delete a Property

**URL:** /property/{id}
**Method:** DELETE
**Authentication:** HTTP Basic
**Response:**
**Status:** 200 OK or 404 Not Found
**Body:**
```json
{
  "message": "Property Deleted."
}
```

#### User Management

##### 1. Greeting Message

**Endpoint:** /user/
**Method:** GET
**Authentication:** HTTP Basic
**Response Status:** 200 OK
**Description:** Returns a greeting message.
**Response:**
```json
{
  "message": "Greetings!! Glad to see you here"
}
```
##### 2. Fetch User Profile

**Endpoint:** /user/profile
**Method:** GET
**Authentication:** HTTP Basic
**Response Status:** 200 OK
**Description:** Retrieves the profile information of the authenticated user.
**Response:**
```json
{
  "message": "User retrieved.",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "1234567890",
    "password": "encoded_password",
    "properties": []
  }
}
```
##### 3. Update User Profile

**Endpoint:** /user/update
**Method:** PUT
**Authentication:** HTTP Basic
**Response Status:** 202 Accepted
**Description:** Updates the authenticated user's profile.
**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "new_password",
  "phone": "1234567890"
}
```
**Response**
```json
{
  "message": "User details updated.",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "1234567890",
    "password": "encoded_password",
    "properties": []
  }
}
```
##### 4. Delete User Account

**Endpoint:** /user/delete
**Method:** DELETE
**Authentication:** HTTP Basic
**Response Status:** 202 Accepted
**Description:** Deletes the authenticated user account and associated properties.
**Response:**
```json
{
  "message": "User and associated properties removed."
}
```
