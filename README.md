# Setting up Docker

1. Prerequisites

- Docker (>= 20.x)
- Docker Compose (>= 2.x)
- Recommended: At least 2 CPU cores, 4GB RAM

2. Build Spring Boot JAR

Before Docker can run the app, we need the JAR file:

- This is for **Maven** only:

```
mvn clean package -DskipTests
```

-> This will generate something like target/jobportal-0.0.1-SNAPSHOT.jar.

3. Create a **Dockerfile**

Inside the project root (same level as pom.mxl), create a file named Dockerfile and copy paste below:

```
# Step 1: Use an official JDK image to build/run the app
FROM openjdk:17-jdk-slim

# Step 2: Set working directory inside the container
WORKDIR /app

# Step 3: Copy the JAR file into the container
COPY target/jobportal-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Tell Docker how to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]

# Step 5: Expose the port Spring Boot runs on
EXPOSE 8080
```

4. Build the Docker Image

In the project folder (where the Dockerfile is), run:

```
docker build -t jobportal:1.0 .
```

- -t jobportal:1.0 → tags the image with a name and version.

5. Run the Container

To start the container:

```
docker run -p 8080:8080 jobportal:1.0
```

- -p 8080:8080 → maps container port 8080 to our local 8080.
- jobportal:1.0 → the image that just built.

# Spring Boot + Database (Full Setup)

So far, the container runs the Spring Boot app only. For a full setup:

- Use _docker-compose_ to run Spring Boot + MySQL (any database) together.
- That way, the backend can connect to a database container instead of local DB.

## What is Docker Compose?

- Docker alone runs one container at a time (like the first one what that we did)
- But real projects need multiple containers (e.g. app + database)
- **Docker Compose** is a tool where we define all containers and their networking in **one YAML** file (_docker-compose.yml_).

Example of _docker-compose.yml_ that can be use as a starting point:

```
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: jobportal-mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: jobportal
      MYSQL_USER: jobuser
      MYSQL_PASSWORD: jobpass
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  app:
    build: .
    container_name: jobportal-api
    restart: always
    depends_on:
      - mysql
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/jobportal
      SPRING_DATASOURCE_USERNAME: jobuser
      SPRING_DATASOURCE_PASSWORD: jobpass

volumes:
  mysql_data:
```

- mysql service

  - Uses the official mysql:8.0 image.
  - Creates a database jobportal with user jobuser/jobpass.
  - Exposes port 3306 so we can still connect with MySQL Workbench if needed.
  - Persists data using a volume (mysql_data).

- app service
  - Builds the Spring Boot app from the Dockerfile (build: .).
  - Waits for MySQL (depends_on).
  - Connects to DB using mysql hostname (Docker automatically provides internal networking).

## Next Steps

1. Put this _docker-compose.yml_ in the project root(where the Dockerfile is)
2. Run:

```
docker-compose up --build
```

- this will build the Spring Boot app, run MySQL, and link them together

# Useful Docker Commands

## Container Management

```
docker ps                        # List running containers
docker ps -a                     # List all containers (including stopped ones)
docker start <name>              # Start a stopped container
docker stop <name>               # Stop a running container
docker restart <name>            # Restart a container
docker rm <name>                 # Remove a stopped container
docker logs -f <name>            # View container logs (follow mode)
docker exec -it <name> /bin/sh   # Open shell inside a container
```

## Image Management

```
docker images          # List images
docker build -t myapp:latest .   # Build image from Dockerfile
docker rmi <image_id>  # Remove an image
```

## System Cleanup

```
docker system df       # Show disk usage
docker system prune    # Remove unused containers, networks, images
docker system prune -af   # Force remove everything (careful!)
```

## Docker Compose

```
docker-compose up      # Start services (foreground)
docker-compose up -d   # Start services in background
docker-compose down    # Stop and remove containers, networks
docker-compose build   # Rebuild images
docker-compose restart # Restart all services
docker-compose logs -f # View logs for all services
docker-compose ps      # List containers in the current project
```

## Volume & Network

```
docker volume ls       # List volumes
docker volume rm <vol> # Remove volume (careful: deletes data)
docker network ls      # List networks
docker network inspect <name> # Inspect network details
```
