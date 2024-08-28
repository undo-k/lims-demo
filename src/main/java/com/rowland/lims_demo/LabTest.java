package com.rowland.lims_demo;

import jakarta.persistence.*;

@Entity
public class LabTest {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TestType testType;

    @OneToOne
    private Sample sample;


    public LabTest(TestType testType, Sample sample) {
        this.testType = testType;
        this.sample = sample;
    }

    protected LabTest(){}

    public enum TestType {FUN, SMELL, SOUND}
}
