package com.rowland.lims_demo;


import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class RestTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private PatientRepository repo;

    @Test
    public void postThenGetPatients() throws Exception {

        Patient john = new Patient("John", "Smith", "1963-11-22", "M");

        String patientJson = String.format("{\"firstName\": \"%s\", \"lastName\": \"%s\", \"dateOfBirth\": \"%s\", \"sex\": \"%s\"}", john.getFirstName(), john.getLastName(), john.getDateOfBirth().toString(), john.getSex());
        mvc.perform(MockMvcRequestBuilders.post("/api/patients").contentType(MediaType.APPLICATION_JSON).content(patientJson));

        mvc.perform(MockMvcRequestBuilders.get("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("firstName").value(john.getFirstName()))
                .andExpect(jsonPath("lastName").value(john.getLastName()));
    }
}
