package com.rowland.lims_demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/{id}")
    public Patient findById(@PathVariable Long id) {
        return patientRepository.findById(id).orElseThrow(PatientNotFoundException::new);
    }

    @PutMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Patient create(@RequestBody Patient patient) {
        Patient existingPatient = patientRepository.findByFirstNameAndLastNameAndDateOfBirth(patient.getFirstName(), patient.getLastName(), patient.getDateOfBirth());

        if (existingPatient != null) {
            return existingPatient;
        } else {
            return patientRepository.save(patient);
        }
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Patient> update(@RequestBody Patient patient) {
        Patient existingPatient = patientRepository.findByFirstNameAndLastNameAndDateOfBirth(patient.getFirstName(), patient.getLastName(), patient.getDateOfBirth());

        if (existingPatient != null) {
            existingPatient.setAddress(patient.getAddress());
            return new ResponseEntity<>(patientRepository.save(existingPatient), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    static private class PatientNotFoundException extends RuntimeException {
        public PatientNotFoundException() {
            super("Patient not found");
        }
    }

}
