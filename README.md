# Simple-Calendar-System
<img width="1918" height="967" alt="img" src="https://github.com/user-attachments/assets/94ffd81c-c61c-40e7-b92e-3b133c030234" />

A full-stack application with React frontend, Java backend, and MySQL database.

## Prerequisites

Before you begin, ensure you have the following installed on your system:

- [Git](https://git-scm.com/downloads)
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Getting Started

Follow these steps to get the project up and running on your local machine.

### 1. Clone the Repository
You can either clone the repository or download it as a ZIP archive.
```bash
git clone https://github.com/Code-Alien/Simple-Calendar-System
cd Simple-Calendar-System
```

### 2. Start the Application

Run the following command to build and start all services:

```bash
docker compose up
```

This will start three services:
- **Frontend** (React with Vite) on port 3000
- **Backend** (Java) on port 8080
- **MySQL Database** on port 3306

To run in detached mode (in the background):

```bash
docker compose up -d
```

The first build may take several minutes as Docker downloads images and installs dependencies.

### 3. Access the Application

Once all containers are running, you can access:

- **Frontend Application**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **MySQL Database**: `localhost:3306`
  - Database: `mydb`
  - User: `root`
  - Password: `example`
