package com.rowland.lims_demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabOrderRepository extends CrudRepository<LabOrder, Long> {

    LabOrder findById(long id);

}