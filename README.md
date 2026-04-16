# Media Server

Media Server is a personal media archival system built with Spring Boot, designed to handle video and image uploads, processing, and archival.

## Features

- **Image Upload**: API endpoint to upload image files with validation and processing.
- **Video Upload**: API endpoint to upload video files with validation and processing. (Not implemented yet)
- **Message Queue Integration**: Asynchronous processing using RabbitMQ.
- **Archival to HDD**: Archive files to local HDD for long-term storage.
- **Archival to Cloud**: Archive files to cloud storage for long-term storage. (Not implemented yet)
- **Metadata Extraction**: Extract and manage metadata from images and videos. (Not implemented yet)
- **Thumbnail Generation**: Generate thumbnails for images and videos. (Not implemented yet)
- **Gallery**: View and search uploaded files. (Not implemented yet)

## Roadmap

- [x] Phase 1: A working REST API that accepts files and saves them to SSD (hot storage).
- [x] Phase 2: RabbitMQ integration and Archive Service to decouple the upload process from the archive process (to HDD - cold storage).
- [ ] Phase 3: Metadata extraction and thumbnail generation.
- [ ] Phase 4: Gallery service to view and search uploaded files.
- [ ] Phase 5: Cloud storage integration for long-term storage.

## Architecture

Files flow through a simple pipeline:

1. The **Upload Service** receives a file via REST, validates it, and publishes a message to RabbitMQ.
2. The **Archive Service** consumes messages from the queue and moves files to the configured storage destination.
3. The **Metadata Service** consumes messages from the queue and extracts metadata from the file.
4. The **Thumbnail Service** consumes messages from the queue and generates thumbnails for the file.


## Tech Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.2.x
- **Build Tool**: Gradle
- **Database**: PostgreSQL
- **Message Queue**: RabbitMQ
- **Containerization**: Docker & Docker Compose

## Project Structure

```
media-server/
├── common/                  # Shared libraries and DTOs
│   └── src/main/java/
│       └── dto/             # Shared request/response models
├── upload-service/          # Handles file intake via REST API
│   └── src/main/java/
│       ├── controller/      # REST endpoints
│       ├── config/          # Configuration
│       ├── exception/       # Exception handling
│       └── service/         # Business logic
├── archive-service/         # Consumes queue and writes to storage
│   └── src/main/java/
│       ├── config/          # Configuration
│       └── service/         # Archive logic
└── build.gradle             # Root build configuration
```

## Setup

### Prerequisites

- **Java 21**
- **Docker**
- **Docker Compose**

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd media-server
   ```

2. **Configure environment variables**
   Copy the example env file and update the paths to match your system:
   ```bash
   cp .env.example .env
   ```
   Note: Update the paths to match your system. Make sure the paths exist on your host machine.

3. **Start services with Docker Compose**
   This will start all the services and RabbitMQ.
   ```bash
   docker-compose up -d --build
   ```