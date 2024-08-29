package com.rowland.lims_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/physician/")
public class PhysicianController {

    @Autowired
    PhysicianRepository physicianRepository;

    @GetMapping("/{id}")
    private Physician findById(@PathVariable Long id) {
        return physicianRepository.findById(id).orElseThrow(PhysicianNotFoundException::new);
    }

    @PutMapping("/create")
    private ResponseEntity<RestResponse> create(@RequestBody Physician physician) {
        Physician existingPhysician = physicianRepository.findByFirstNameAndLastNameAndHospital(physician.getFirstName(), physician.getLastName(), physician.getHospital());

        if (existingPhysician != null) {
            RestResponse response = new RestResponse("Physician already exists", null);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Physician saved = physicianRepository.save(physician);
            RestResponse response = new RestResponse("Successfully created Physician", saved.getId());

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

    @PutMapping("/update")
    private ResponseEntity<RestResponse> update(@RequestBody Physician physician) {
        Optional<Physician> optionalPhysician = physicianRepository.findById(physician.getId());

        if (optionalPhysician.isEmpty()) {
            return new ResponseEntity<>(new RestResponse("Physician does not exist", null), HttpStatus.NOT_FOUND);
        }


        Physician existingPhysician = optionalPhysician.get();
        if (Objects.equals(existingPhysician, physician)) {
            return new ResponseEntity<>(new RestResponse("Physician details are the same", null), HttpStatus.OK);
        }

        existingPhysician = LimsHelper.copyFields(existingPhysician, physician, Physician.class, "firstName", "lastName", "hospital", "insuranceNetwork");

        physicianRepository.save(existingPhysician);
        return new ResponseEntity<>(new RestResponse("Successfully updated physician details", null), HttpStatus.ACCEPTED);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    static private class PhysicianNotFoundException extends RuntimeException {
        public PhysicianNotFoundException() {
            super("Physician not found");
        }
    }

    static private class RestResponse {
        private Long id;
        private String message;

        private RestResponse(String message, Long id) {
            this.message = message;
            this.id = id;
        }

        protected RestResponse() {};

        public Long getId() {
            return id;
        }

        public String getMessage() {
            return message;
        }
    }



}
