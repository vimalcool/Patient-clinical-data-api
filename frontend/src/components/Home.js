import { useEffect, useState } from 'react';
import '../App.css';
import { Link } from 'react-router-dom';
import { patientsApi } from '../api';

function Home() {
	const [patients, setPatients] = useState([]);
	const [isLoading, setIsLoading] = useState(true);
	const [error, setError] = useState('');

	useEffect(() => {
		let isMounted = true;

		const fetchPatients = async () => {
			try {
				const response = await patientsApi.get('/patients');
				const responseData = response.data;
				const patientList = Array.isArray(responseData)
					? responseData
					: responseData?.patients || responseData?.data || [];

				if (isMounted) {
					setPatients(Array.isArray(patientList) ? patientList : []);
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

		fetchPatients();

		return () => {
			isMounted = false;
		};
	}, []);

	const columns = patients.length > 0
		? [...new Set(patients.flatMap((patient) => Object.keys(patient)))]
		: [];

	if (isLoading) {
		return <p>Loading patient details...</p>;
	}

	if (error) {
		return <p role="alert">{error}</p>;
	}

	return (
		<main>
			<h1>Patient Details</h1>
			{patients.length === 0 ? (
				<p>No patients found.</p>
			) : (
				<table>
					<thead>
						<tr>
							<th scope="col">First Name</th>
							<th scope="col">Last Name</th>
							<th scope="col">Age</th>
							<th scope="col">Clinicals</th>
						</tr>
					</thead>
					<tbody>
						{patients.map((patient, index) => (
							<tr key={patient.id || patient._id || index}>
								{columns.map((column) => (
									<td key={column}>
										{typeof patient[column] === 'object' && patient[column] !== null
											? JSON.stringify(patient[column])
											: String(patient[column] ?? '')}
									</td>
								))}
								<td>
									<Link to={`/clinicaldata/clinicals/${encodeURIComponent(patient.id || patient._id)}`}>
										Add clinical
									</Link>
								</td>
							</tr>
						))}
					</tbody>
				</table>
        
			)}
      <Link to="/add-patient" className="add-patient-link">Add Patient</Link>
		</main>
	);
}

export default Home;
