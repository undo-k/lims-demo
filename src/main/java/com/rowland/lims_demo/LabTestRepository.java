package com.rowland.lims_demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabTestRepository extends CrudRepository<LabTest, Long> {
    LabTest findById(long id);
}
