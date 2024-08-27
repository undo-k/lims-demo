package com.rowland.lims_demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;


public interface PatientRepository extends CrudRepository<Patient, Long> {
    Patient findByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, LocalDate dateOfBirth);

    Patient findById(long id);
}
