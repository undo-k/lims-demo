package com.rowland.lims_demo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


import java.util.HashSet;
import java.util.Set;


@Entity
public class LabOrder {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Patient patient;

    @ManyToOne()
    private Physician physician;

    @OneToMany
    private Set<LabTest> tests;

    @Enumerated(EnumType.STRING)
    private Status status;


    public LabOrder(Patient patient, Physician physician, HashSet<LabTest> tests){
        this.patient = patient;
        this.physician = physician;
        this.tests = tests;
        this.status = Status.IN_PROGRESS;
    }

    protected LabOrder(){}

    public Long getId() {
        return id;
    }
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }


    public Physician getPhysician() {
        return physician;
    }

    public void setPhysician(Physician physician) {
        this.physician = physician;
    }

    public Set<LabTest> getTests() {
        return tests;
    }

    public void setTests(HashSet<LabTest> tests) {
        this.tests = tests;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }



    public enum Status {AWAITING_SAMPLES, IN_PROGRESS, COMPLETE, CANCELLED}

}
