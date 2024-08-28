package com.rowland.lims_demo;

import org.springframework.data.repository.CrudRepository;


public interface LabOrderRepository extends CrudRepository<LabOrder, Long> {

    LabOrder findById(long id);

}