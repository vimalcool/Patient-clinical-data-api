package com.vimal.clinicalsapi.patientsdata.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vimal.clinicalsapi.patientsdata.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
