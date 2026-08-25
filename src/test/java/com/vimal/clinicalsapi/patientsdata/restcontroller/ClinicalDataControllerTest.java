package com.vimal.clinicalsapi.patientsdata.restcontroller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.vimal.clinicalsapi.patientsdata.dto.ClinicalDataRequest;
import com.vimal.clinicalsapi.patientsdata.model.ClinicalData;
import com.vimal.clinicalsapi.patientsdata.model.Patient;
import com.vimal.clinicalsapi.patientsdata.repos.ClinicalDataRepository;
import com.vimal.clinicalsapi.patientsdata.repos.PatientRepository;

@ExtendWith(MockitoExtension.class)
class ClinicalDataControllerTest {

        @Mock
        private ClinicalDataRepository clinicalDataRepository;

        @Mock
        private PatientRepository patientRepository;

        private ClinicalDataController clinicalDataController;

        @BeforeEach
        void setUp() {
                clinicalDataController = new ClinicalDataController(
                                clinicalDataRepository,
                                patientRepository);
        }

        @Test
        void saveClinicalData_shouldReturnBadRequestWhenPatientIdIsMissing() {
                ClinicalDataRequest request = new ClinicalDataRequest();
                request.setComponentName("bp");
                request.setComponentValue("67/119");

                ResponseEntity<?> response = clinicalDataController.saveClinicalData(request);

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("patientId is required", response.getBody());

                verifyNoInteractions(patientRepository, clinicalDataRepository);
        }

        @Test
        void saveClinicalData_shouldReturnNotFoundWhenPatientDoesNotExist() {
                ClinicalDataRequest request = createRequest(99L);

                when(patientRepository.findById(99L))
                                .thenReturn(Optional.empty());

                ResponseEntity<?> response = clinicalDataController.saveClinicalData(request);

                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                verify(patientRepository).findById(99L);
                verifyNoInteractions(clinicalDataRepository);
        }

        @Test
        void saveClinicalData_shouldSaveClinicalDataForExistingPatient() {
                Patient patient = new Patient("John", "Doe", 35);
                ClinicalDataRequest request = createRequest(1L);
                ClinicalData savedClinicalData = new ClinicalData();

                when(patientRepository.findById(1L))
                                .thenReturn(Optional.of(patient));
                when(clinicalDataRepository.save(any(ClinicalData.class)))
                                .thenReturn(savedClinicalData);

                ResponseEntity<?> response = clinicalDataController.saveClinicalData(request);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertSame(savedClinicalData, response.getBody());

                ArgumentCaptor<ClinicalData> captor = ArgumentCaptor.forClass(ClinicalData.class);

                verify(clinicalDataRepository).save(captor.capture());

                ClinicalData clinicalData = captor.getValue();

                assertEquals("bp", clinicalData.getComponentName());
                assertEquals("67/119", clinicalData.getComponentValue());
                assertSame(patient, clinicalData.getPatient());

                verify(patientRepository).findById(1L);
        }

        @Test
        void saveClinicalData_shouldPassTheCorrectPatientIdToRepository() {
                ClinicalDataRequest request = createRequest(5L);
                Patient patient = new Patient("Jane", "Smith", 28);

                when(patientRepository.findById(5L))
                                .thenReturn(Optional.of(patient));
                when(clinicalDataRepository.save(any(ClinicalData.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                clinicalDataController.saveClinicalData(request);

                verify(patientRepository, times(1)).findById(5L);
        }

        @Test
        void saveClinicalData_shouldNotSaveWhenPatientIsMissing() {
                ClinicalDataRequest request = createRequest(10L);

                when(patientRepository.findById(10L))
                                .thenReturn(Optional.empty());

                clinicalDataController.saveClinicalData(request);

                verify(clinicalDataRepository, never()).save(any(ClinicalData.class));
        }

        @Test
        void saveClinicalData_shouldPropagateRepositoryException() {
                ClinicalDataRequest request = createRequest(1L);
                Patient patient = new Patient("John", "Doe", 35);
                RuntimeException exception = new RuntimeException("Database error");

                when(patientRepository.findById(1L))
                                .thenReturn(Optional.of(patient));
                when(clinicalDataRepository.save(any(ClinicalData.class)))
                                .thenThrow(exception);

                RuntimeException thrown = assertThrows(
                                RuntimeException.class,
                                () -> clinicalDataController.saveClinicalData(request));

                assertSame(exception, thrown);
                verify(patientRepository).findById(1L);
                verify(clinicalDataRepository).save(any(ClinicalData.class));
        }

        private ClinicalDataRequest createRequest(Long patientId) {
                ClinicalDataRequest request = new ClinicalDataRequest();
                request.setComponentName("bp");
                request.setComponentValue("67/119");
                request.setPatientId(patientId);
                return request;
        }
}