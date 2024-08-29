package com.rowland.lims_demo;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RestController
@RequestMapping("/api/order/")
public class LabOrderController {
    @Autowired LabOrderRepository labOrderRepository;

    @Autowired LabTestRepository labTestRepository;

    @GetMapping("/{id}")
    public LabOrder findById(@PathVariable Long id) {
        return labOrderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<RestResponse> create(@RequestBody LabOrder order) {
        Patient patient = order.getPatient();
        Physician physician = order.getPhysician();
        List<LabTest> tests = order.getTests();

        if (patient == null || patient.getId() == null) {
            return new ResponseEntity<>(new RestResponse("Missing patient"), HttpStatus.BAD_REQUEST);
        }

        if (physician == null || physician.getId() == null) {
            return new ResponseEntity<>(new RestResponse("Missing physician"), HttpStatus.BAD_REQUEST);
        }

        if (tests == null || tests.isEmpty()) {
            return new ResponseEntity<>(new RestResponse("Missing test"), HttpStatus.BAD_REQUEST);
        }

        for(LabTest test : tests) {
            labTestRepository.save(test);
        }

        order.setTests(tests);
        Long orderId = labOrderRepository.save(order).getId();
        return new ResponseEntity<>(new RestResponse("Successfully created Order", orderId), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    private ResponseEntity<RestResponse> update(@RequestBody LabOrder order) {
        Optional<LabOrder> optionalLabOrder = labOrderRepository.findById(order.getId());

        if (optionalLabOrder.isEmpty()) {
            return new ResponseEntity<>(new RestResponse("Order does not exist"), HttpStatus.NOT_FOUND);
        }

        LabOrder existingOrder = optionalLabOrder.get();

        LimsHelper.copyFields(existingOrder, order, LabOrder.class, "physician", "status");
        if (order.getTests() != null && !Objects.equals(existingOrder.getTests(), order.getTests())) {
            existingOrder.setTests(order.getTests());

            for(LabTest test : existingOrder.getTests()) {
                labTestRepository.save(test);
            }
        }

        labOrderRepository.save(existingOrder);
        return new ResponseEntity<>(new RestResponse("Successfully updated order"), HttpStatus.ACCEPTED);
    }

    @PutMapping("/linksample")
    private ResponseEntity<RestResponse> linkSample(@RequestBody LinkSampleBody request) {
        Sample sample = request.getSample();
        Long testId = request.getTestId();
        if (sample == null || testId == null) {
            return new ResponseEntity<>(new RestResponse("Missing Sample or Test ID"), HttpStatus.BAD_REQUEST);
        }

        Optional<LabTest> optionalLabTest = labTestRepository.findById(testId);

        if (optionalLabTest.isEmpty()) {
            return new ResponseEntity<>(new RestResponse("Test does not exist"), HttpStatus.BAD_REQUEST);
        }

        LabTest test = optionalLabTest.get();

        test.setSample(sample);
        labTestRepository.save(test);

        return new ResponseEntity<>(new RestResponse("Sample linked to Test and Order", test.getOrder().getId()), HttpStatus.ACCEPTED);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    static private class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException() {
            super("Order not found");
        }
    }

    static private class RestResponse {
        private Long id;
        private String message;

        private RestResponse(String message, Long id) {
            this.message = message;
            this.id = id;
        }

        private RestResponse(String message) {
            this.message = message;
        }

        protected RestResponse() {};

        public Long getId() {
            return id;
        }

        public String getMessage() {
            return message;
        }
    }

    static private class LinkSampleBody {
        private Sample sample;
        private Long testId;

        public Sample getSample() {
            return sample;
        }

        public Long getTestId() {
            return testId;
        }
    }


}
