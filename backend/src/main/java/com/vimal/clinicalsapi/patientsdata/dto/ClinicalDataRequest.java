package com.vimal.clinicalsapi.patientsdata.dto;

/**
 * ClinicalDataRequest is a DTO (Data Transfer Object) class that represents a
 * request to create or update clinical data for a patient.
 * It contains the necessary fields to capture the clinical data information,
 * including the component name, component value, measured date and time, and
 * the associated patient ID.
 */
public class ClinicalDataRequest {

    private String componentName;
    private String componentValue;
    private Long patientId;

    // Getters and Setters
    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentValue() {
        return componentValue;
    }

    public void setComponentValue(String componentValue) {
        this.componentValue = componentValue;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
