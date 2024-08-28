package com.rowland.lims_demo;

import jakarta.persistence.*;

import java.util.Set;

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
