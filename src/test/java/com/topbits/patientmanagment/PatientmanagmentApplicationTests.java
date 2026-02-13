package com.topbits.patientmanagment;

import com.topbits.patientmanagment.api.dto.response.PatientResponse;
import com.topbits.patientmanagment.common.exception.NotFoundException;
import com.topbits.patientmanagment.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PatientmanagmentApplicationTests {

    @Autowired
    PatientService patientService;
    @Test
    void getPatientByIdNotFoundTest() {
        assertThrows(NotFoundException.class, () -> patientService.getById(999999L));
    }

    @Test
    void getPatientByIdTest() {
        PatientResponse patient=patientService.getById(4L);
        assertEquals(4, patient.getId());
    }

}
