# Warehouse API

## Overview

This repository contains the server-side implementation of the "Warehouse API" application, built with Spring Boot.

## Table of Contents

- [Installation](#installation)
- [API](#api)
  - [1. Registration](#1-registration)
  - [2. Login](#2-login)
  - [3. Orders](#3-orders)
    - [3.1 Create Order](#31-create-order)
    - [3.2 Fetch All Orders](#32-fetch-all-orders)
    - [3.3 Fetch Order by ID](#33-fetch-order-by-id)
    - [3.4 Delete Order by ID](#34-delete-order-by-id)
  - [4. Products](#4-products)
    - [4.1 Fetch Product by ID](#41-fetch-product-by-id)
    - [4.2 Fetch All Products](#42-fetch-all-products)
    - [4.3 Create Product](#43-create-product)
    - [4.4 Update Product by ID](#44-update-product-by-id)
    - [4.5 Delete Product by ID](#45-delete-product-by-id)
  - [5. Product Types](#5-product-types)
    - [5.1 Fetch Product Type by ID](#51-fetch-product-type-by-id)
    - [5.2 Fetch All Product Types](#52-fetch-all-product-types)
    - [5.3 Create Product Type](#53-create-product-type)
    - [5.4 Update Product Type by ID](#54-update-product-type-by-id)
    - [5.5 Delete Product Type by ID](#55-delete-product-type-by-id)
  - [6. Stores](#6-stores)
    - [6.1 Fetch Store by ID](#61-fetch-store-by-id)
    - [6.2 Fetch All Stores](#62-fetch-all-stores)
    - [6.3 Create Store](#63-create-store)
    - [6.4 Update Store by ID](#64-update-store-by-id)
    - [6.5 Delete Store by ID](#65-delete-store-by-id)
  - [7. Suppliers](#7-suppliers)
    - [7.1 Fetch Supplier by ID](#71-fetch-supplier-by-id)
    - [7.2 Fetch All Suppliers](#72-fetch-all-suppliers)
    - [7.3 Create Supplier](#73-create-supplier)
    - [7.4 Update Supplier by ID](#74-update-supplier-by-id)
    - [7.5 Delete Supplier by ID](#75-delete-supplier-by-id)

## Installation

1. **Clone the repository:**

    ```bash
    git clone https://github.com/yourusername/warehouse-api.git
    cd warehouse-api
    ```

2. **Install dependencies with Gradle:**

    ```bash
    ./gradlew build
    ```

3. **Run the server:**

    ```bash
    ./gradlew bootRun
    ```

## API

### 1. Registration

Register a new account.

- **Endpoint:** `POST /api/v1/auth/register`

- **Request Parameters:**
    - `username` (String): User's login.
    - `password` (String): User's password.
    - `email` (String): User's email.
    - `firstname` (String): User's first name.
    - `surname` (String): User's surname.

- **Example:**

    ```bash
    curl -X POST -H "Content-Type: application/json" -d '{"username": "john_doe", "password": "securepassword", "email": "john.doe@example.com", "firstname": "John", "surname": "Doe"}' http://localhost:8080/api/v1/auth/register
    ```

### 2. Login

Authenticate a user.

- **Endpoint:** `POST /api/v1/auth/login`

- **Request Parameters:**
    - `username` (String): User's login.
    - `password` (String): User's password.

- **Example:**

    ```bash
    curl -X POST -H "Content-Type: application/json" -d '{"username": "john_doe", "password": "securepassword"}' http://localhost:8080/api/v1/auth/login
    ```

### 3. Orders

#### 3.1 Create Order

- **Endpoint:** `POST /api/v1/orders`

- **Request Parameters:**
    - `orderType` (String): Type of the order.
    - `orderDate` (LocalDateTime): Date of the order.
    - `amount` (Integer): Amount of the order.
    - `storeId` (Long): ID of the store.
    - `supplierId` (Long): ID of the supplier.
    - `orderCompositionRequestList` (List<OrderCompositionRequest>): List of order compositions.

- **Example:**

    ```bash
    curl -X POST -H "Content-Type: application/json" -d '{"orderType": "Purchase", "orderDate": "2024-02-01T12:00:00", "amount": 100, "storeId": 1, "supplierId": 2, "orderCompositionRequestList": [{"productId": 1, "quantity": 10}, {"productId": 2, "quantity": 5}]}' http://localhost:8080/api/v1/orders
    ```

#### 3.2 Fetch All Orders

- **Endpoint:** `GET /api/v1/orders`

- **Example:**

    ```bash
    curl http://localhost:8080/api/v1/orders
    ```

#### 3.3 Fetch Order by ID

- **Endpoint:** `GET /api/v1/orders/{orderId}`

- **Request Parameters:**
    - `orderId` (Long): ID of the order.

- **Example:**

    ```bash
    curl http://localhost:8080/api/v1/orders/1
    ```

#### 3.4 Delete Order by ID

- **Endpoint:** `DELETE /api/v1/orders/{orderId}`

- **Request Parameters:**
    - `orderId` (Long): ID of the order.

- **Example:**

    ```bash
    curl -X DELETE http://localhost:8080/api/v1/orders/1
    ```

### 4. Products

#### 4.1 Fetch Product by ID

- **Endpoint:** `GET /api/v1/products/{productId}`

- **Request Parameters:**
  - `productId` (Long): ID of the product.

- **Example:**
    ```bash
    curl http://localhost:8080/api/v1/products/1
    ```

#### 4.2 Fetch All Products

- **Endpoint:** `GET /api/v1/products`

- **Example:**
    ```bash
    curl http://localhost:8080/api/v1/products
    ```

#### 4.3 Create Product

- **Endpoint:** `POST /api/v1/products`

- **Request Parameters:**
  - `title` (String): Title of the product.
  - `date` (LocalDateTime): Date of the product.
  - `cost` (Integer): Cost of the product.
  - `description` (String): Description of the product.
  - `productTypeId` (Long): ID of the product type.

- **Example:**
    ```bash
    curl -X POST -H "Content-Type: application/json" -d '{"title": "New Product", "date": "2024-02-01T12:00:00", "cost": 50, "description": "A new product", "productTypeId": 1}' http://localhost:8080/api/v1/products
    ```

#### 4.4 Update Product by ID

- **Endpoint:** `PATCH /api/v1/products/{productId}`

- **Request Parameters:**
  - `productId` (Long): ID of the product.
  - `title` (String): Updated title of the product.
  - `date` (LocalDateTime): Updated date of the product.
  - `cost` (Integer): Updated cost of the product.
  - `description` (String): Updated description of the product.
  - `productTypeId` (Long): Updated ID of the product type.

- **Example:**
    ```bash
    curl -X PATCH -H "Content-Type: application/json" -d '{"title": "Updated Product", "date": "2024-02-02T12:00:00", "cost": 60, "description": "An updated product", "productTypeId": 2}' http://localhost:8080/api/v1/products/1
    ```

#### 4.5 Delete Product by ID

- **Endpoint:** `DELETE /api/v1/products/{productId}`

- **Request Parameters:**
  - `productId` (Long): ID of the product.

- **Example:**
    ```bash
    curl -X DELETE http://localhost:8080/api/v1/products/1
    ```

### 5. ProductTypes

#### 5.1 Fetch Product Type by ID

- **Endpoint:** `GET /api/v1/types/{productTypeId}`

- **Request Parameters:**
  - `productTypeId` (Long): ID of the product type.

- **Example:**
    ```bash
    curl http://localhost:8080/api/v1/types/1
    ```

#### 5.2 Fetch All Product Types

- **Endpoint:** `GET /api/v1/types`

- **Example:**
    ```bash
    curl http://localhost:8080/api/v1/types
    ```

#### 5.3 Create Product Type

- **Endpoint:** `POST /api/v1/types`

- **Request Parameters:**
  - `name` (String): Name of the product type.

- **Example:**
    ```bash
    curl -X POST -H "Content-Type: application/json" -d '{"name": "New Product Type"}' http://localhost:8080/api/v1/types
    ```

#### 5.4 Update Product Type by ID

- **Endpoint:** `PATCH /api/v1/types/{productTypeId}`

- **Request Parameters:**
  - `productTypeId` (Long): ID of the product type.
  - `name` (String): Updated name of the product type.

- **Example:**
    ```bash
    curl -X PATCH -H "Content-Type: application/json" -d '{"name": "Updated Product Type"}' http://localhost:8080/api/v1/types/1
    ```

#### 5.5 Delete Product Type by ID

- **Endpoint:** `DELETE /api/v1/types/{productTypeId}`

- **Request Parameters:**
  - `productTypeId` (Long): ID of the product type.

- **Example:**
    ```bash
    curl -X DELETE http://localhost:8080/api/v1/types/1
    ```

### 6. Stores

#### 6.1 Fetch Store by ID

- **Endpoint:** `GET /api/v1/stores/{storeId}`

- **Request Parameters:**
  - `storeId` (Long): ID of the store.

- **Example:**
    ```bash
    curl http://localhost:8080/api/v1/stores/1
    ```

#### 6.2 Fetch All Stores

- **Endpoint:** `GET /api/v1/stores`

- **Example:**
    ```bash
    curl http://localhost:8080/api/v1/stores
    ```

#### 6.3 Create Store

- **Endpoint:** `POST /api/v1/stores`

- **Request Parameters:**
  - `name` (String): Name of the store.

- **Example:**
    ```bash
    curl -X POST -H "Content-Type: application/json" -d '{"name": "New Store"}' http://localhost:8080/api/v1/stores
    ```

#### 6.4 Update Store by ID

- **Endpoint:** `PATCH /api/v1/stores/{storeId}`

- **Request Parameters:**
  - `storeId` (Long): ID of the store.
  - `name` (String): Updated name of the store.

- **Example:**
    ```bash
    curl -X PATCH -H "Content-Type: application/json" -d '{"name": "Updated Store"}' http://localhost:8080/api/v1/stores/1
    ```

#### 6.5 Delete Store by ID

- **Endpoint:** `DELETE /api/v1/stores/{storeId}`

- **Request Parameters:**
  - `storeId` (Long): ID of the store.

- **Example:**
    ```bash
    curl -X DELETE http://localhost:8080/api/v1/stores/1
    ```

### 7. SupplierController

#### 7.1 Fetch Supplier by ID

- **Endpoint:** `GET /api/v1/suppliers/{supplierId}`

- **Request Parameters:**
  - `supplierId` (Long): ID of the supplier.

- **Example:**
    ```bash
    curl http://localhost:8080/api/v1/suppliers/1
    ```

#### 7.2 Fetch All Suppliers

- **Endpoint:** `GET /api/v1/suppliers`

- **Example:**
    ```bash
    curl http://localhost:8080/api/v1/suppliers
    ```

#### 7.3 Create Supplier

- **Endpoint:** `POST /api/v1/suppliers`

- **Request Parameters:**
  - `title` (String): Title of the supplier.
  - `surname` (String): Surname of the supplier.
  - `address` (String): Address of the supplier.
  - `phoneNumber` (String): Phone number of the supplier.

- **Example:**
    ```bash
    curl -X POST -H "Content-Type: application/json" -d '{"title": "New Supplier", "surname": "Doe", "address": "123 Main St", "phoneNumber": "+1234567890"}' http://localhost:8080/api/v1/suppliers

#### 7.4 Update Supplier by ID

- **Endpoint:** `PATCH /api/v1/suppliers/{supplierId}`

- **Request Parameters:**
  - `supplierId` (Long): ID of the supplier.
  - `title` (String): Updated title of the supplier.
  - `surname` (String): Updated surname of the supplier.
  - `address` (String): Updated address of the supplier.
  - `phoneNumber` (String): Updated phone number of the supplier.

- **Example:**
    ```bash
    curl -X PATCH -H "Content-Type: application/json" -d '{"title": "Updated Supplier", "surname": "Smith", "address": "456 Oak St", "phoneNumber": "+9876543210"}' http://localhost:8080/api/v1/suppliers/1
    ```

#### 7.5 Delete Supplier by ID

- **Endpoint:** `DELETE /api/v1/suppliers/{supplierId}`

- **Request Parameters:**
  - `supplierId` (Long): ID of the supplier.

- **Example:**
    ```bash
    curl -X DELETE http://localhost:8080/api/v1/suppliers/1
    ```