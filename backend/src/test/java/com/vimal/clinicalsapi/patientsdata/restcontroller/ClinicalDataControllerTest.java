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

                ClinicalData response = clinicalDataController.saveClinicalData(request);

                assertEquals("patientId is required", response.getComponentName());

                verifyNoInteractions(patientRepository, clinicalDataRepository);
        }

        @Test
        void saveClinicalData_shouldReturnNotFoundWhenPatientDoesNotExist() {
                ClinicalDataRequest request = createRequest(99L);

                when(patientRepository.findById(99L))
                                .thenReturn(Optional.empty());

                ClinicalData response = clinicalDataController.saveClinicalData(request);

                assertEquals("patientId is required", response.getComponentName());
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

                ClinicalData response = clinicalDataController.saveClinicalData(request);

                assertSame(savedClinicalData, response);

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
                ClinicalData savedClinicalData = new ClinicalData();

                when(patientRepository.findById(5L))
                                .thenReturn(Optional.of(patient));
                when(clinicalDataRepository.save(any(ClinicalData.class)))
                                .thenReturn(savedClinicalData);

                ClinicalData response = clinicalDataController.saveClinicalData(request);

                assertSame(savedClinicalData, response);
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