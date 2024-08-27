package com.rowland.lims_demo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Entity
public class Patient {
    private enum Sex {M, F};

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    private String address;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Sex sex;




    public Patient(String firstName, String lastName, String dateOfBirth, String sex) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = LocalDate.parse(dateOfBirth);
        this.sex = Sex.valueOf(sex.toUpperCase());
    }

    protected Patient() {
    }

    @Override
    public String toString() {
        return String.format(
                "Patient[id=%d, firstName='%s', lastName='%s', dateOfBirth=%s, sex=%s]",
                id, firstName, lastName, dateOfBirth, sex);
    }


    public Long getId() {
        return id;
    }

    public @NotNull String getFirstName() {
        return firstName;
    }

    public @NotNull String getLastName() {
        return lastName;
    }

    public @NotNull LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = LocalDate.parse(dateOfBirth);
    }

    public @NotNull Sex getSex() {
        return sex;
    }

}

