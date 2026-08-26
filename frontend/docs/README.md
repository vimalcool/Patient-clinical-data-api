# Clinical 3 Documentation

## Overview

Clinical 3 is a React front-end for managing patient records and clinical observations. It provides a compact workflow for:

- viewing the patient list
- creating a new patient record
- adding clinical components for a selected patient

The app uses React Router for navigation, Axios for backend requests, and toast notifications for user feedback.

## Architecture

### Front-end stack

- React 19
- React Router DOM 7
- Axios
- React Toastify
- Create React App tooling

### Main application flow

- The app shell is defined in [src/App.js](../src/App.js)
- API configuration is centralized in [src/api.js](../src/api.js)
- View logic is split into three main page components:
  - [src/components/Home.js](../src/components/Home.js)
  - [src/components/AddPatient.js](../src/components/AddPatient.js)
  - [src/components/AddClinical.js](../src/components/AddClinical.js)

## Routes

| Route | Purpose |
| --- | --- |
| / | Displays the patient list |
| /add-patient | Creates a new patient |
| /clinicaldata/clinicals/:patientId | Adds clinical data to a specific patient |

## Features

### 1. Patient list

The home page calls the backend endpoint /patients and renders the returned records in a table. It also exposes a link that navigates to the patient form.

The component:

- shows a loading state while the request is in progress
- surfaces an error if the patient list cannot be loaded
- displays an empty state when no patients are returned
- links each patient to the clinical form using their patient id

### 2. Add patient

The Add Patient screen accepts:

- first name
- last name
- age

On submit, it sends a POST request to /patients and resets the form after a successful response. Validation is handled through required fields and HTML input constraints.

### 3. Add clinical data

The Add Clinical Data screen:

- reads the patient id from the route parameter
- fetches patient details from /patients/:patientId
- displays the selected patient name and age
- allows the user to enter a component name and value
- sends a POST request to /clinicaldata/clinicals with patientId, componentName, and componentValue

## API configuration

The client is configured in [src/api.js](../src/api.js) with:

- base URL: REACT_APP_PATIENTS_API_URL or /patient-services/api
- JSON content type header

Expected backend endpoints used by the app:

- GET /patients
- POST /patients
- GET /patients/:patientId
- POST /clinicaldata/clinicals

## User experience notes

- Toast notifications are shown for success, errors, and loading states.
- The app uses BrowserRouter and client-side routing.
- The page layout is intentionally simple and focused on form-first patient management.

## Local development

### Required dependencies

This app needs the following packages installed for it to run correctly:

- react
- react-dom
- react-router-dom
- axios
- react-toastify
- @testing-library/react
- @testing-library/jest-dom
- @testing-library/user-event
- web-vitals

Install everything with:

```bash
npm install
```

If needed, the key runtime packages are:

```bash
npm install react react-dom react-router-dom axios react-toastify
```

No extra UI framework or component package is required; the app uses standard React components and custom CSS.

### Start the app

```bash
npm start
```

The application runs in development mode on http://localhost:3000.

### Build for production

```bash
npm run build
```

## Environment expectations

The app expects a backend service available at the configured API base path. If no environment variable is set, it defaults to /patient-services/api.

In a local environment, this is usually paired with an API server running on the same host or behind the configured proxy.

## Important implementation notes

- The project is using a proxy entry in [package.json](../package.json): http://localhost:8080.
- The generated CRA default README is generic and does not reflect the actual patient-management workflow; this documentation describes the real functionality of the app.
