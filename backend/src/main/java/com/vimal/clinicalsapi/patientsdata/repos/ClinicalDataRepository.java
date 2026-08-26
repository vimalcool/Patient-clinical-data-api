package com.vimal.clinicalsapi.patientsdata.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vimal.clinicalsapi.patientsdata.model.ClinicalData;

public interface ClinicalDataRepository extends JpaRepository<ClinicalData, Long> {
}