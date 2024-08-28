package com.rowland.lims_demo;


import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    private ObjectMapper objectMapper;


    String createPatientURI = "/api/patients/create";
    String updatePatientURI = "/api/patients/update";
    String getPatientURI = "/api/patients/";

    @Test
    public void CreateThenGetPatient() throws Exception {

        Patient john = new Patient("John", "Smith", "1963-11-22", "M");

        objectMapper.writeValueAsString(john);

        String patientJson = objectMapper.writeValueAsString(john);
        mvc.perform(MockMvcRequestBuilders.put(createPatientURI).contentType(MediaType.APPLICATION_JSON).content(patientJson));
        mvc.perform(MockMvcRequestBuilders.put(createPatientURI).contentType(MediaType.APPLICATION_JSON).content(patientJson));
        mvc.perform(MockMvcRequestBuilders.put(createPatientURI).contentType(MediaType.APPLICATION_JSON).content(patientJson));
        mvc.perform(MockMvcRequestBuilders.put(createPatientURI).contentType(MediaType.APPLICATION_JSON).content(patientJson));
        mvc.perform(MockMvcRequestBuilders.put(createPatientURI).contentType(MediaType.APPLICATION_JSON).content(patientJson));

        mvc.perform(MockMvcRequestBuilders.get(getPatientURI + "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("firstName").value(john.getFirstName()))
                .andExpect(jsonPath("lastName").value(john.getLastName()));
        mvc.perform(MockMvcRequestBuilders.get(getPatientURI + "2").contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void UpdateThenGetPatient() throws Exception {

        Patient john = new Patient("John", "Smith", "1963-11-22", "M");


        String patientJson = objectMapper.writeValueAsString(john);
        mvc.perform(MockMvcRequestBuilders.put(createPatientURI).contentType(MediaType.APPLICATION_JSON).content(patientJson));

        john.setAddress("Wallaby Way");
        patientJson = objectMapper.writeValueAsString(john);
        mvc.perform(MockMvcRequestBuilders.put(updatePatientURI).contentType(MediaType.APPLICATION_JSON).content(patientJson));

        john.setAddress("Spain");
        patientJson = objectMapper.writeValueAsString(john);
        mvc.perform(MockMvcRequestBuilders.put(updatePatientURI).contentType(MediaType.APPLICATION_JSON).content(patientJson));

        john.setAddress("sdfdfgdfgdf56yugh5itdfg3w");
        patientJson = objectMapper.writeValueAsString(john);
        mvc.perform(MockMvcRequestBuilders.put(updatePatientURI).contentType(MediaType.APPLICATION_JSON).content(patientJson));

        john.setAddress("9c7c5fbb210b50e97c27a20583c099538f594f2422925654055fdb2484edb6f6");
        patientJson = objectMapper.writeValueAsString(john);
        mvc.perform(MockMvcRequestBuilders.put(updatePatientURI).contentType(MediaType.APPLICATION_JSON).content(patientJson));

        mvc.perform(MockMvcRequestBuilders.get(getPatientURI + "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("firstName").value(john.getFirstName()))
                .andExpect(jsonPath("lastName").value(john.getLastName()))
                .andExpect(jsonPath("address").value("9c7c5fbb210b50e97c27a20583c099538f594f2422925654055fdb2484edb6f6"));

        mvc.perform(MockMvcRequestBuilders.get(getPatientURI + "2").contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
