package com.rowland.lims_demo;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/{id}")
    public Patient findById(@PathVariable Long id) {
        return patientRepository.findById(id).orElseThrow(PatientNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient create(@RequestBody Patient patient) {

        return patientRepository.save(patient);
    }

}

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException() {
        super("Patient not found");
    }
}

//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
//class PatientNotFoundException extends RuntimeException {
//    public PatientNotFoundException() {
//        super("Patient not found");
//    }
//}