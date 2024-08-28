package com.rowland.lims_demo;


import jakarta.persistence.Entity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/order")
public class LabOrderController {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired LabOrderRepository labOrderRepository;

    @GetMapping("/{id}")
    public LabOrder findById(@PathVariable Long id) {
        return labOrderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LabOrder> create(@RequestBody LabOrder order) {
        Patient patient = order.getPatient();
        Physician physician = order.getPhysician();
        Set<LabTest> tests = order.getTests();

        if (patient == null || patient.getId() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (physician == null || physician.getId() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (tests == null || tests.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(labOrderRepository.save(order), HttpStatus.CREATED);
    }

//    @PutMapping("/update")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public ResponseEntity<Patient> update(@RequestBody Patient patient) {
//        Patient existingPatient = patientRepository.findByFirstNameAndLastNameAndDateOfBirth(patient.getFirstName(), patient.getLastName(), patient.getDateOfBirth());
//
//        if (existingPatient != null) {
//            existingPatient.setAddress(patient.getAddress());
//            return new ResponseEntity<>(patientRepository.save(existingPatient), HttpStatus.ACCEPTED);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    static private class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException() {
            super("Order not found");
        }
    }


}
