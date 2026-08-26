import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../App.css';
import { patientsApi } from '../api';
import { toast } from 'react-toastify';

function AddPatient() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    "firstname": '',
    "lastname": '',
    "age": '',
  });
  const [isSaving, setIsSaving] = useState(false);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((currentData) => ({ ...currentData, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    toast.info('Loading patient details.');
    toast.warning('Please check the entered age.');
    toast.error('Request failed.');
    toast.dismiss();

    try {
      await patientsApi.post('/patients', {
        "firstname": formData.firstname.trim(),
        "lastname": formData.lastname.trim(),
        "age": Number(formData.age),
      });
      toast.success('Patient saved successfully.');
      setFormData({ "firstname": '', "lastname": '', "age": '' });
    } catch {
      toast.error('Unable to save patient. Please try again.');
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <main>
      <h1>Add Patient</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="firstname">First name</label>
          <input
            id="firstname"
            name="firstname"
            type="text"
            value={formData.firstname}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label htmlFor="lastname">Last name</label>
          <input
            id="lastname"
            name="lastname"
            type="text"
            value={formData.lastname}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label htmlFor="age">Age</label>
          <input
            id="age"
            name="age"
            type="number"
            min="0"
            max="150"
            value={formData.age}
            onChange={handleChange}
            required
          />
        </div>
        <button type="submit" disabled={isSaving}>
          {isSaving ? 'Saving...' : 'Save patient'}
        </button>
      </form>
      <button type="button" onClick={() => navigate('/')}>
        Back to patients
      </button>
      <p><Link to="/">View patient list</Link></p>
    </main>
  );
}

export default AddPatient;
