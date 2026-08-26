package com.vimal.clinicalsapi.patientsdata.restcontroller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vimal.clinicalsapi.patientsdata.dto.ClinicalDataRequest;
import com.vimal.clinicalsapi.patientsdata.model.ClinicalData;
import com.vimal.clinicalsapi.patientsdata.model.Patient;
import com.vimal.clinicalsapi.patientsdata.repos.ClinicalDataRepository;
import com.vimal.clinicalsapi.patientsdata.repos.PatientRepository;

@RestController
@RequestMapping("/api/clinicaldata")
public class ClinicalDataController {

    private static final Logger logger = LoggerFactory.getLogger(ClinicalDataController.class);
    @Autowired
    private ClinicalDataRepository clinicalDataRepository;

    @Autowired
    private PatientRepository patientRepository;

    public ClinicalDataController(ClinicalDataRepository clinicalDataRepository,
            PatientRepository patientRepository) {
        this.clinicalDataRepository = clinicalDataRepository;
        this.patientRepository = patientRepository;
    }

    @PostMapping
    public ClinicalData createClinicalData(@RequestBody ClinicalData clinicalData) {
        logger.debug("Creating clinical data: {}", clinicalData);
        return clinicalDataRepository.save(clinicalData);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ClinicalData> getAllClinicalData() {
        logger.debug("Fetching all clinical data");
        return clinicalDataRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicalData> getClinicalDataById(@PathVariable Long id) {
        logger.debug("Fetching clinical data with id {}", id);
        return clinicalDataRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClinicalData> updateClinicalData(
            @PathVariable Long id,
            @RequestBody ClinicalData dataDetails) {

        logger.debug("Updating clinical data with id {}", id);
        return clinicalDataRepository.findById(id)
                .map(clinicalData -> {
                    clinicalData.setComponentName(dataDetails.getComponentName());
                    clinicalData.setComponentValue(dataDetails.getComponentValue());
                    clinicalData.setMeasuredDateTime(dataDetails.getMeasuredDateTime());

                    return ResponseEntity.ok(
                            clinicalDataRepository.save(clinicalData));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClinicalData(@PathVariable Long id) {
        logger.debug("Deleting clinical data with id {}", id);
        if (!clinicalDataRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        clinicalDataRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/clinicals")
    public ClinicalData saveClinicalData(
            @RequestBody ClinicalDataRequest request) {

        logger.debug("Saving clinical data for patient: {}", request.getPatientId());
        if (request.getPatientId() == null) {
            throw new IllegalArgumentException("patientId is required");
        }

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Patient not found with id: " + request.getPatientId()));

        ClinicalData clinicalData = new ClinicalData();
        clinicalData.setComponentName(request.getComponentName());
        clinicalData.setComponentValue(request.getComponentValue());
        clinicalData.setPatient(patient);

        return clinicalDataRepository.save(clinicalData);
    }
}
