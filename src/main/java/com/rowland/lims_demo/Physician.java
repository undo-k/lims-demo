package com.rowland.lims_demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.util.List;


@Entity
public class Physician {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String hospital;

    @NotNull
    private List<String> insuranceNetwork;


    public Physician(String firstName, String lastName, String hospital, List<String> insuranceNetwork) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.hospital = hospital;
        this.insuranceNetwork = insuranceNetwork;
    }

    protected Physician() {}

    public Long getId() {
        return id;
    }

    public void setFirstName(@NotNull String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@NotNull String lastName) {
        this.lastName = lastName;
    }

    public @NotNull String getFirstName() {
        return firstName;
    }

    public @NotNull String getLastName() {
        return lastName;
    }

    public @NotNull String getHospital() {
        return hospital;
    }

    public void setHospital(@NotNull String hospital) {
        this.hospital = hospital;
    }

    public @NotNull List<String> getInsuranceNetwork() {
        return insuranceNetwork;
    }

    public void setInsuranceNetwork(@NotNull List<String> insuranceNetwork) {
        this.insuranceNetwork = insuranceNetwork;
    }
}
