package com.rowland.lims_demo;



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



    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    static private class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException() {
            super("Order not found");
        }
    }


}
