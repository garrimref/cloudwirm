# Cloud File Storage

Multi-user Cloud file Storage is a simple cloud-based file storage service that enables users to upload and manage their files.
Built using Java and Spring Boot, this project provide a environment, ensuring each user has access to their own private storage space.

## Features
- User Management: Handles user registration, login, and session management.
- File Storage: Upload, display, navigate, and manage files stored in MinIO (S3-compatible storage).
- (In development) Search Functionality: Allows users to quickly search files based on name or metadata.
- Deployment: Docker Compose integration for easy setup and deployment.

## Built with
- Backend: Java, Spring Boot
- Frontend: Thymeleaf, Bootstrap
- Database: MySQL
- File Storage: MinIO (S3)
- Session Management: Redis
- Deployment: Docker Compose
- Test: UnitTests and Testcontainers
- Build: Maven

## Future Plans
I plan to deploy this project on a cloud server in the near future to provide easier access. 
