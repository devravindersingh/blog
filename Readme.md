# Project 2: Blog API with Persistence and Caching

## Overview
This project is a RESTful Blog API built with Spring Boot, designed to manage blog posts with persistence, caching, asynchronous operations, and administrative controls. It demonstrates key concepts like database persistence (PostgreSQL), caching (Redis), asynchronous task scheduling, TCP socket programming for admin commands, and comprehensive logging for debugging and monitoring.

## Features
- **Full CRUD API for Blog Posts**:
    - `POST /api/posts`: Create a new post (asynchronous).
    - `GET /api/posts/{id}`: Retrieve a post by ID (cached in Redis).
    - `GET /api/posts`: List all posts (cached in Redis).
    - `PUT /api/posts/{id}`: Update a post (asynchronous, evicts cache).
    - `DELETE /api/posts/{id}`: Delete a post (asynchronous, clears caches).
- **Persistence**:
    - Posts are stored in a PostgreSQL database.
    - Uses Spring Data JPA (`JpaRepository`) for database operations.
- **Caching**:
    - Redis caching for `GET` requests to reduce database load.
    - Cache invalidation on updates and deletes using `@CacheEvict`.
    - Logs cache hits/misses at `TRACE` level.
- **Asynchronous Operations**:
    - Post creation, updates, and deletes are handled asynchronously using a custom `TaskScheduler` with a thread pool.
- **Admin Server**:
    - TCP socket server on port 9999 for administrative commands:
        - `shutdown`: Stops the application.
        - `clearcache`: Clears the Redis cache.
        - `status`: Reports the number of posts in the database.
- **Logging**:
    - Request logging for all API endpoints using a Spring `Interceptor`.
    - Detailed logging for admin commands and async task execution.
    - Cache operation logs (hits/misses) via `logging.level.org.springframework.cache=TRACE`.

## Tech Stack
- **Spring Boot**: Framework for building the REST API.
- **Spring Data JPA**: For database persistence with PostgreSQL.
- **Redis**: For caching frequently accessed data.
- **PostgreSQL**: Relational database for storing posts.
- **Kafka**: Not used in this project (will be introduced in Project 3).
- **Java Concurrency**: Custom `TaskScheduler` for asynchronous operations.
- **Socket Programming**: TCP socket server for admin commands.
- **SLF4J/Logback**: For logging application behavior.

## Prerequisites
- Java 17
- Maven
- PostgreSQL (running on `localhost:5432`, with database `postgres`, user `postgres`, password `postgres`)
- Redis (running on `localhost:6379`)
- `curl` (for testing API endpoints and admin commands)

## Setup
1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd blog-api