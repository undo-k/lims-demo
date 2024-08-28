package com.rowland.lims_demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Physician {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;


    protected Physician() {}

    public Long getId() {
        return id;
    }
}
