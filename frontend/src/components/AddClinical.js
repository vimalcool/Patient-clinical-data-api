import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import '../App.css';
import { patientsApi } from '../api';
import { toast } from 'react-toastify';

function AddClinical() {
	const { patientId } = useParams();
	const [patient, setPatient] = useState(null);
	const [isLoading, setIsLoading] = useState(true);
	const [isSaving, setIsSaving] = useState(false);
	const [formData, setFormData] = useState({
		componentName: '',
		componentValue: '',
	});
	const [error, setError] = useState('');

	const handleChange = (event) => {
		const { name, value } = event.target;
		setFormData((currentData) => ({ ...currentData, [name]: value }));
	};

	const handleSubmit = async (event) => {
		event.preventDefault();
		setIsSaving(true);

		try {
			await patientsApi.post(`/clinicaldata/clinicals`, {
				componentName: formData.componentName.trim(),
				componentValue: formData.componentValue.trim(),
				patientId: patientId,
			});
			toast.success('Clinical component saved successfully.');
			setFormData({ componentName: '', componentValue: '' });
		} catch {
			toast.error('Unable to save the clinical component.');
		} finally {
			setIsSaving(false);
		}
	};

	useEffect(() => {
		let isMounted = true;

		const fetchPatient = async () => {
			try {
				const response = await patientsApi.get(`/patients/${patientId}`);
				const responseData = response.data;
				const patientData = responseData?.data || responseData?.patient || responseData;

				if (isMounted) {
					setPatient(patientData);
					setError('');
				}
			} catch {
				if (isMounted) {
					setError('Unable to load patient details.');
				}
			} finally {
				if (isMounted) {
					setIsLoading(false);
				}
			}
		};

		fetchPatient();

		return () => {
			isMounted = false;
		};
	}, [patientId]);

	if (isLoading) {
		return <p>Loading patient details...</p>;
	}

	if (error) {
		return <p role="alert">{error}</p>;
	}

	return (
		<main>
			<h1>Add Clinical Data</h1>
			<dl className="patient-details">
				<dd className="patient-name">
					{patient?.firstname ?? ''} {patient?.lastname ?? ''}
				</dd>
				<dt>Age: {patient?.age ?? ''}</dt>
				
			</dl>
			
			<form onSubmit={handleSubmit}>
				<div>
					<label htmlFor="componentName">Component name</label>
					<input
						id="componentName"
						name="componentName"
						type="text"
						value={formData.componentName}
						onChange={handleChange}
						required
					/>
				</div>
				<div>
					<label htmlFor="componentValue">Component value</label>
					<input
						id="componentValue"
						name="componentValue"
						type="text"
						value={formData.componentValue}
						onChange={handleChange}
						required
					/>
				</div>
				<button type="submit" disabled={isSaving} align="center">
					{isSaving ? 'Saving...' : 'Save'}
				</button>
			</form>
			<Link to="/">Back to patients</Link>
		</main>
	);
}

export default AddClinical;
