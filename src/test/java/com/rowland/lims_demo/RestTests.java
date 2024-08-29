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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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


    final String PATIENT_URI = "/api/patient/";
    final String ORDER_URI = "/api/order/";
    final String PHYSICIAN_URI = "/api/physician/";
    final String CREATE = "/create";
    final String UPDATE = "/update";
            

    @Test
    public void CreateThenGetPatient() throws Exception {

        Patient john = new Patient("John", "Smith", "1963-11-22", "M");

        objectMapper.writeValueAsString(john);

        String patientJson = objectMapper.writeValueAsString(john);
        mvc.perform(MockMvcRequestBuilders.put(PATIENT_URI + CREATE).contentType(MediaType.APPLICATION_JSON).content(patientJson));
        mvc.perform(MockMvcRequestBuilders.put(PATIENT_URI + CREATE).contentType(MediaType.APPLICATION_JSON).content(patientJson));
        mvc.perform(MockMvcRequestBuilders.put(PATIENT_URI + CREATE).contentType(MediaType.APPLICATION_JSON).content(patientJson));
        mvc.perform(MockMvcRequestBuilders.put(PATIENT_URI + CREATE).contentType(MediaType.APPLICATION_JSON).content(patientJson));
        String returnedString = mvc.perform(MockMvcRequestBuilders.put(PATIENT_URI + CREATE).contentType(MediaType.APPLICATION_JSON).content(patientJson)).andReturn().getResponse().getContentAsString();

        Patient returnedPatient = objectMapper.readValue(returnedString, Patient.class);

        mvc.perform(MockMvcRequestBuilders.get(PATIENT_URI + returnedPatient.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("firstName").value(john.getFirstName()))
                .andExpect(jsonPath("lastName").value(john.getLastName()));
        mvc.perform(MockMvcRequestBuilders.get(PATIENT_URI + "1000").contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void UpdateThenGetPatient() throws Exception {

        Patient john = new Patient("John", "Smith", "1963-11-22", "M");


        String patientJson = objectMapper.writeValueAsString(john);
        String returnedString = mvc.perform(MockMvcRequestBuilders.put(PATIENT_URI + CREATE).contentType(MediaType.APPLICATION_JSON).content(patientJson)).andReturn().getResponse().getContentAsString();

        Patient returnedPatient = objectMapper.readValue(returnedString, Patient.class);

        john.setAddress("Wallaby Way");
        patientJson = objectMapper.writeValueAsString(john);
        mvc.perform(MockMvcRequestBuilders.put(PATIENT_URI + UPDATE).contentType(MediaType.APPLICATION_JSON).content(patientJson));

        john.setAddress("Spain");
        patientJson = objectMapper.writeValueAsString(john);
        mvc.perform(MockMvcRequestBuilders.put(PATIENT_URI + UPDATE).contentType(MediaType.APPLICATION_JSON).content(patientJson));

        john.setAddress("sdfdfgdfgdf56yugh5itdfg3w");
        patientJson = objectMapper.writeValueAsString(john);
        mvc.perform(MockMvcRequestBuilders.put(PATIENT_URI + UPDATE).contentType(MediaType.APPLICATION_JSON).content(patientJson));

        john.setAddress("9c7c5fbb210b50e97c27a20583c099538f594f2422925654055fdb2484edb6f6");
        patientJson = objectMapper.writeValueAsString(john);
        mvc.perform(MockMvcRequestBuilders.put(PATIENT_URI + UPDATE).contentType(MediaType.APPLICATION_JSON).content(patientJson));

        mvc.perform(MockMvcRequestBuilders.get(PATIENT_URI + returnedPatient.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("firstName").value(john.getFirstName()))
                .andExpect(jsonPath("lastName").value(john.getLastName()))
                .andExpect(jsonPath("address").value("9c7c5fbb210b50e97c27a20583c099538f594f2422925654055fdb2484edb6f6"));

        mvc.perform(MockMvcRequestBuilders.get(PATIENT_URI + "20000").contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test @Transactional
    public void CreateThenGetOrder() throws Exception {
        Patient patient = new Patient("John", "Smith", "1963-11-22", "M");
        entityManager.persistAndFlush(patient);

        Sample sample = new Sample();
        entityManager.persistAndFlush(sample);

        LabTest test = new LabTest(LabTest.TestType.FUN, sample);
        entityManager.persistAndFlush(test);

        Physician physician = new Physician("Doctor", "Doctor", "Hospital", Arrays.asList("Insurance A", "Insurance B"));
        entityManager.persistAndFlush(physician);

        HashSet<LabTest> labTests = new HashSet<>(List.of(test));
        LabOrder order = new LabOrder(patient, physician, labTests);

        String orderJson = objectMapper.writeValueAsString(order);


         String returnedString = mvc.perform(MockMvcRequestBuilders.post(ORDER_URI + CREATE).contentType(MediaType.APPLICATION_JSON).content(orderJson)).andReturn().getResponse().getContentAsString();

         LabOrder returnedOrder = objectMapper.readValue(returnedString, LabOrder.class);

        mvc.perform(MockMvcRequestBuilders.get(ORDER_URI + returnedOrder.getId())
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

        Physician physician = new Physician("Doctor", "Doctor", "Hospital", Arrays.asList("Insurance A", "Insurance B"));
        entityManager.persistAndFlush(physician);

        HashSet<LabTest> labTests = new HashSet<>(List.of(test));
        LabOrder order = new LabOrder(patient, physician, labTests);

        String orderJson = objectMapper.writeValueAsString(order);
        String returnedString = mvc.perform(MockMvcRequestBuilders.post(ORDER_URI + CREATE).contentType(MediaType.APPLICATION_JSON).content(orderJson))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();
        LabOrder returnedOrder = objectMapper.readValue(returnedString, LabOrder.class);

        returnedOrder.setStatus(LabOrder.Status.COMPLETE);
        orderJson = objectMapper.writeValueAsString(returnedOrder);

         mvc.perform(MockMvcRequestBuilders.put(ORDER_URI + UPDATE).contentType(MediaType.APPLICATION_JSON).content(orderJson)).andReturn().getResponse().getContentAsString();


        mvc.perform(MockMvcRequestBuilders.get(ORDER_URI + returnedOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("patient.id").value(patient.getId()))
                .andExpect(jsonPath("patient.lastName").value(patient.getLastName()))
                .andExpect(jsonPath("status").value("COMPLETE"));

    }
    
    @Test
    public void CreateThenGetPhysician() throws Exception {
        Physician physician = new Physician("Doctor", "Doctor", "Hospital", Arrays.asList("Insurance A", "Insurance B"));
        String physicianJson = objectMapper.writeValueAsString(physician);

        mvc.perform(MockMvcRequestBuilders.put(PHYSICIAN_URI + CREATE).contentType(MediaType.APPLICATION_JSON).content(physicianJson)).andExpect(MockMvcResultMatchers.status().isCreated());
        mvc.perform(MockMvcRequestBuilders.put(PHYSICIAN_URI + CREATE).contentType(MediaType.APPLICATION_JSON).content(physicianJson)).andExpect(MockMvcResultMatchers.status().isOk());
        mvc.perform(MockMvcRequestBuilders.get(PHYSICIAN_URI + "2").contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void CreateThenUpdatePhysician() throws Exception {
        Physician physician = new Physician("Doctor", "Doctor2", "Hospital", Arrays.asList("Insurance A", "Insurance B"));
        String physicianJson = objectMapper.writeValueAsString(physician);

        String returnedString = mvc.perform(MockMvcRequestBuilders.put(PHYSICIAN_URI + CREATE).contentType(MediaType.APPLICATION_JSON).content(physicianJson)).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();
        physician = objectMapper.readValue(returnedString, Physician.class);
        assert physician.getId() != null;

        physician.setHospital("The Big Hospital");
        physicianJson = objectMapper.writeValueAsString(physician);

        mvc.perform(MockMvcRequestBuilders.put(PHYSICIAN_URI + UPDATE).contentType(MediaType.APPLICATION_JSON).content(physicianJson)).andExpect(MockMvcResultMatchers.status().isAccepted());
        mvc.perform(MockMvcRequestBuilders.put(PHYSICIAN_URI + UPDATE).contentType(MediaType.APPLICATION_JSON).content(physicianJson)).andExpect(MockMvcResultMatchers.status().isAccepted());
    }

}
