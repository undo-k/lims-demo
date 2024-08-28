package com.rowland.lims_demo;

import org.springframework.data.repository.CrudRepository;
import java.time.LocalDate;



public interface PatientRepository extends CrudRepository<Patient, Long> {
    Patient findByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, LocalDate dateOfBirth);

    Patient findById(long id);
}
