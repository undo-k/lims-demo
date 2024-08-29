package com.rowland.lims_demo;

import org.springframework.data.repository.CrudRepository;


public interface PhysicianRepository extends CrudRepository<Physician, Long> {

    Physician findByFirstNameAndLastNameAndHospital(String firstName, String lastName, String hospital);
    Physician findById(long id);
}
