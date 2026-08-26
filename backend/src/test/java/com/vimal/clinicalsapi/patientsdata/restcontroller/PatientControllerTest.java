package com.vimal.clinicalsapi.patientsdata.restcontroller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.vimal.clinicalsapi.patientsdata.model.Patient;
import com.vimal.clinicalsapi.patientsdata.repos.PatientRepository;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

    @Mock
    private PatientRepository patientRepository;

    private PatientController patientController;

    @BeforeEach
    void setUp() {
        patientController = new PatientController(patientRepository);
    }

    @Test
    void createPatient_shouldSaveAndReturnPatient() {
        Patient requestPatient = new Patient("John", "Doe", 35);
        when(patientRepository.save(requestPatient)).thenReturn(requestPatient);
        Patient result = patientController.createPatient(requestPatient);
        assertSame(requestPatient, result);
        verify(patientRepository, times(1)).save(requestPatient);
        verifyNoMoreInteractions(patientRepository);
    }

    @Test
    void createPatient_shouldReturnSavedPatient() {
        Patient requestPatient = new Patient("Jane", "Smith", 28);
        Patient savedPatient = new Patient("Jane", "Smith", 28);
        when(patientRepository.save(requestPatient)).thenReturn(savedPatient);
        Patient result = patientController.createPatient(requestPatient);
        assertSame(savedPatient, result);
        verify(patientRepository).save(requestPatient);
    }

    @Test
    void createPatient_shouldPropagateRepositoryException() {
        Patient patient = new Patient("John", "Doe", 35);
        RuntimeException exception = new RuntimeException("Database error");
        when(patientRepository.save(patient)).thenThrow(exception);
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> patientController.createPatient(patient));
        assertSame(exception, thrown);
        verify(patientRepository).save(patient);
    }

    @Test
    void createPatient_shouldPassTheExactPatientToRepository() {
        Patient patient = new Patient("Alice", "Brown", 42);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        patientController.createPatient(patient);
        verify(patientRepository).save(same(patient));
    }
}