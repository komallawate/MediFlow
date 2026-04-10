package com.mediflow.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDate issuedDate;

    @Column(length = 2000)
    private String diagnosis;

    @Column(length = 4000)
    private String medicinesJson;

    @Column(length = 1000)
    private String notes;

    // Constructors
    public Prescription() {}

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Appointment appointment;
        private Patient patient;
        private Doctor doctor;
        private LocalDate issuedDate;
        private String diagnosis;
        private String medicinesJson;
        private String notes;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder appointment(Appointment appointment) { this.appointment = appointment; return this; }
        public Builder patient(Patient patient) { this.patient = patient; return this; }
        public Builder doctor(Doctor doctor) { this.doctor = doctor; return this; }
        public Builder issuedDate(LocalDate issuedDate) { this.issuedDate = issuedDate; return this; }
        public Builder diagnosis(String diagnosis) { this.diagnosis = diagnosis; return this; }
        public Builder medicinesJson(String medicinesJson) { this.medicinesJson = medicinesJson; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }

        public Prescription build() {
            Prescription p = new Prescription();
            p.id = this.id;
            p.appointment = this.appointment;
            p.patient = this.patient;
            p.doctor = this.doctor;
            p.issuedDate = this.issuedDate;
            p.diagnosis = this.diagnosis;
            p.medicinesJson = this.medicinesJson;
            p.notes = this.notes;
            return p;
        }
    }

    // Getters
    public Long getId() { return id; }
    public Appointment getAppointment() { return appointment; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public LocalDate getIssuedDate() { return issuedDate; }
    public String getDiagnosis() { return diagnosis; }
    public String getMedicinesJson() { return medicinesJson; }
    public String getNotes() { return notes; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    public void setIssuedDate(LocalDate issuedDate) { this.issuedDate = issuedDate; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setMedicinesJson(String medicinesJson) { this.medicinesJson = medicinesJson; }
    public void setNotes(String notes) { this.notes = notes; }
}
