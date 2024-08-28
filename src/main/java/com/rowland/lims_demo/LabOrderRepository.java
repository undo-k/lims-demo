package com.rowland.lims_demo;

import org.springframework.data.repository.CrudRepository;


public interface LabOrderRepository extends CrudRepository<LabOrder, Long> {
//    Order findByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, LocalDate dateOfBirth);

    LabOrder findByPatientId(long id);
}