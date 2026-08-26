# Patient Clinical Data API

A Spring Boot REST API for managing patients and their clinical data using Spring Data JPA and MySQL.

## Technology

- Java 17
- Spring Boot 4.2.0-SNAPSHOT
- Spring Web MVC
- Spring Data JPA
- MySQL
- Maven

## Prerequisites

- JDK 17 or later
- MySQL 8 or later
- Git

## Database Setup

Create the database:

```sql
CREATE DATABASE clinicals;
```

Configure the connection in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/clinicals?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

Make sure the database tables match the entity mappings. The `patient` table uses `first_name`, `last_name`, and `age`. The `clinicaldata` table uses `component_name`, `component_value`, `measured_date_time`, and `patient_id`.

## Run Locally

Clone the repository and enter its directory:

```bash
git clone https://github.com/YOUR_USERNAME/patient-clinical-data-api.git
cd patient-clinical-data-api
```

### Backend (Spring Boot API)

Run the application with Maven:

Windows:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

Linux or macOS:

```bash
cd backend
./mvnw spring-boot:run
```

The API is available at:

```text
http://localhost:8080/patient-services
```

### Frontend (React + React Router)

The frontend is located in the `frontend` folder and communicates with the backend through the proxy configured in `frontend/package.json`.

Install dependencies:

```bash
cd frontend
npm install
```

Start the frontend development server:

```bash
npm start
```

The React app runs at:

```text
http://localhost:3000
```

If you want to change the API URL, set the environment variable before starting the app:

```bash
REACT_APP_PATIENTS_API_URL=http://localhost:8080/patient-services npm start
```

The app includes pages to:

- view all patients
- add a new patient
- add clinical data for a selected patient

## Frontend Components Required

The frontend is built with React and includes the following key UI components:

- `Home` component: displays the patient list and a link to add patient records
- `AddPatient` component: form to capture patient details such as first name, last name, and age
- `AddClinical` component: form to add a clinical reading for a selected patient
- `App` component: sets up routing and wraps the app with toast notifications
- `api.js`: Axios client used to call the backend REST endpoints

These components are located under `frontend/src/components/` and are required to support the patient management flow.

## Project Structure

```text
patient-clinical-data-api/
├── backend/                 # Spring Boot REST API
│   ├── src/main/java/       # Java source code
│   ├── src/main/resources/  # Config and properties
│   └── pom.xml              # Maven project file
├── frontend/                # React frontend
│   ├── src/                 # UI components and API client
│   ├── public/              # Static assets
│   └── package.json         # React app dependencies and scripts
├── README.md
└── .gitignore
```

## REST Endpoints

### Patients

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/patients` | Create a patient |
| `GET` | `/patients` | Get all patients |
| `GET` | `/patients/{id}` | Get a patient by ID |
| `PUT` | `/patients/{id}` | Update a patient |
| `DELETE` | `/patients/{id}` | Delete a patient |

Example patient request:

```json
{
  "firstname": "John",
  "lastname": "Doe",
  "age": 35
}
```

Full URL example:

```text
POST http://localhost:8080/patient-services/patients
```

### Clinical Data

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/clinicaldata` | Create clinical data directly |
| `GET` | `/clinicaldata` | Get all clinical data |
| `GET` | `/clinicaldata/{id}` | Get clinical data by ID |
| `PUT` | `/clinicaldata/{id}` | Update clinical data |
| `DELETE` | `/clinicaldata/{id}` | Delete clinical data |
| `POST` | `/clinicaldata/clinicals` | Create clinical data for an existing patient |

Use `/clinicaldata/clinicals` when associating a record with a patient:

```json
{
  "componentName": "bp",
  "componentValue": "67/119",
  "patientId": 1
}
```

The `patientId` is required and must identify an existing patient.

## Logging

Logs are written to the console and to:

```text
logs/patient-clinical-data-api.log
```

Logging is configured in `src/main/resources/application.properties`. The application package uses `DEBUG` logging, while the root logger uses `INFO`.

## Run Tests

Windows:

```powershell
.\mvnw.cmd test
```

Linux or macOS:

```bash
./mvnw test
```

## Create a GitHub Repository

1. On GitHub, select **New repository**.
2. Enter `patient-clinical-data-api` as the repository name.
3. Do not add an additional README if this project already contains one.
4. Create the repository and copy its HTTPS URL.
5. From the project directory, run:

```bash
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/patient-clinical-data-api.git
git push -u origin main
```

Replace `YOUR_USERNAME` with your GitHub username.

Never commit database passwords, API keys, or other secrets. Use environment variables or a local configuration file for sensitive values.
