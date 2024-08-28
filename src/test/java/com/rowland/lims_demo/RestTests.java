package com.rowland.lims_demo;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
public class RestTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private PatientRepository repo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestEntityManager entityManager;



    String createPatientURI = "/api/patients/create";
    String updatePatientURI = "/api/patients/update";
    String getPatientURI = "/api/patients/";
    String createOrderUri = "/api/order/create";
    String updateOrderURI = "/api/order/update";
    String getOrderUri = "/api/order/";

    @Test
    public void CreateThenGetPatient() throws Exception {

        Patient john = new Patient("John", "Smith", "1963-11-22", "M");

        objectMapper.writeValueAsString(john);

        String patientJson = objectMapper.writeValueAsString(john);
        mvc.perform(MockMvcRequestBuilders.put(createPatientURI).contentType(MediaType.APPLICATION_JSON).content(patientJson));
        mvc.perform(MockMvcRequestBuilders.put(createPatientURI).contentType(MediaType.APPLICATION_JSON).content(patientJson));
        mvc.perform(MockMvcRequestBuilders.put(createPatientURI).contentType(MediaType.APPLICATION_JSON).content(patientJson));
        mvc.perform(MockMvcRequestBuilders.put(createPatientURI).contentType(MediaType.APPLICATION_JSON).content(patientJson));
        String returnedString = mvc.perform(MockMvcRequestBuilders.put(createPatientURI).contentType(MediaType.APPLICATION_JSON).content(patientJson)).andReturn().getResponse().getContentAsString();

        Patient returnedPatient = objectMapper.readValue(returnedString, Patient.class);

        mvc.perform(MockMvcRequestBuilders.get(getPatientURI + returnedPatient.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("firstName").value(john.getFirstName()))
                .andExpect(jsonPath("lastName").value(john.getLastName()));
        mvc.perform(MockMvcRequestBuilders.get(getPatientURI + "1000").contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void UpdateThenGetPatient() throws Exception {

        Patient john = new Patient("John", "Smith", "1963-11-22", "M");


        String patientJson = objectMapper.writeValueAsString(john);
        String returnedString = mvc.perform(MockMvcRequestBuilders.put(createPatientURI).contentType(MediaType.APPLICATION_JSON).content(patientJson)).andReturn().getResponse().getContentAsString();

        Patient returnedPatient = objectMapper.readValue(returnedString, Patient.class);

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

        mvc.perform(MockMvcRequestBuilders.get(getPatientURI + returnedPatient.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("firstName").value(john.getFirstName()))
                .andExpect(jsonPath("lastName").value(john.getLastName()))
                .andExpect(jsonPath("address").value("9c7c5fbb210b50e97c27a20583c099538f594f2422925654055fdb2484edb6f6"));

        mvc.perform(MockMvcRequestBuilders.get(getPatientURI + "20000").contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test @Transactional
    public void CreateThenGetOrder() throws Exception {
        Patient patient = new Patient("John", "Smith", "1963-11-22", "M");
        entityManager.persistAndFlush(patient);

        Sample sample = new Sample();
        entityManager.persistAndFlush(sample);

        LabTest test = new LabTest(LabTest.TestType.FUN, sample);
        entityManager.persistAndFlush(test);

        Physician physician = new Physician();
        entityManager.persistAndFlush(physician);

        HashSet<LabTest> labTests = new HashSet<>(List.of(test));
        LabOrder order = new LabOrder(patient, physician, labTests);

        String orderJson = objectMapper.writeValueAsString(order);


         String returnedString = mvc.perform(MockMvcRequestBuilders.post(createOrderUri).contentType(MediaType.APPLICATION_JSON).content(orderJson)).andReturn().getResponse().getContentAsString();

         LabOrder returnedOrder = objectMapper.readValue(returnedString, LabOrder.class);

        mvc.perform(MockMvcRequestBuilders.get(getOrderUri + returnedOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("patient.id").value(patient.getId()))
                .andExpect(jsonPath("patient.lastName").value(patient.getLastName()));

    }

    @Test @Transactional
    public void CreateThenUpdateOrder() throws Exception {
        Patient patient = new Patient("Amy", "Jones", "1942-11-22", "F");
        entityManager.persistAndFlush(patient);

        Sample sample = new Sample();
        entityManager.persistAndFlush(sample);

        LabTest test = new LabTest(LabTest.TestType.FUN, sample);
        entityManager.persistAndFlush(test);

        Physician physician = new Physician();
        entityManager.persistAndFlush(physician);

        HashSet<LabTest> labTests = new HashSet<>(List.of(test));
        LabOrder order = new LabOrder(patient, physician, labTests);

        String orderJson = objectMapper.writeValueAsString(order);
        String returnedString = mvc.perform(MockMvcRequestBuilders.post(createOrderUri).contentType(MediaType.APPLICATION_JSON).content(orderJson)).andReturn().getResponse().getContentAsString();
        LabOrder returnedOrder = objectMapper.readValue(returnedString, LabOrder.class);

        returnedOrder.setStatus(LabOrder.Status.COMPLETE);
        orderJson = objectMapper.writeValueAsString(returnedOrder);

         mvc.perform(MockMvcRequestBuilders.put(updateOrderURI).contentType(MediaType.APPLICATION_JSON).content(orderJson)).andReturn().getResponse().getContentAsString();


        mvc.perform(MockMvcRequestBuilders.get(getOrderUri + returnedOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("patient.id").value(patient.getId()))
                .andExpect(jsonPath("patient.lastName").value(patient.getLastName()))
                .andExpect(jsonPath("status").value("COMPLETE"));

    }

}
