import './App.css';
import { BrowserRouter, Route } from 'react-router-dom';
import { Routes } from 'react-router-dom';
import AddPatient from './components/AddPatient';
import AddClinical from './components/AddClinical';
import Home from './components/Home';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
      <Routes>  
        <Route path="/" element={<Home />} />
        <Route path="/add-patient" element={<AddPatient />} />
        <Route path="/clinicaldata/clinicals/:patientId" element={<AddClinical />} />
      </Routes>     
      </BrowserRouter>
       <ToastContainer position="bottom-center" autoClose={2000} />
    </div>
  );
}

export default App;
