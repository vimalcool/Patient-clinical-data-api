import axios from 'axios';

const patientsApiUrl =
  process.env.REACT_APP_PATIENTS_API_URL || '/patient-services/api';

export const patientsApi = axios.create({
  baseURL: patientsApiUrl,
  headers: {
    'Content-Type': 'application/json',
  },
});