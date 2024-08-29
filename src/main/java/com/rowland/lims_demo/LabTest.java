package com.rowland.lims_demo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class LabTest {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TestType testType;

    @OneToOne
    private Sample sample;

    @ManyToOne
    @JoinColumn(name="order_id") @JsonBackReference
    private LabOrder order;


    public LabTest(TestType testType, Sample sample) {
        this.testType = testType;
        this.sample = sample;
    }

    public LabTest(TestType testType) {
        this.testType = testType;
    }

    protected LabTest(){}

    public enum TestType {FUN, SMELL, SOUND}

    public Long getId() {
        return id;
    }

    public @NotNull TestType getTestType() {
        return testType;
    }

    public void setTestType(@NotNull TestType testType) {
        this.testType = testType;
    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    public LabOrder getOrder() {
        return order;
    }
    public void setOrder(LabOrder order) {
        this.order = order;
    }
}
