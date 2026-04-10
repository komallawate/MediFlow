package com.mediflow.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private LocalTime appointmentTime;

    private int queueNumber;

    @Enumerated(EnumType.STRING)
    private Status status = Status.BOOKED;

    private String cancelReason;

    public enum Status { BOOKED, IN_PROGRESS, COMPLETED, CANCELLED }

    // Constructors
    public Appointment() {}

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Patient patient;
        private Doctor doctor;
        private LocalDate appointmentDate;
        private LocalTime appointmentTime;
        private int queueNumber;
        private Status status = Status.BOOKED;
        private String cancelReason;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder patient(Patient patient) { this.patient = patient; return this; }
        public Builder doctor(Doctor doctor) { this.doctor = doctor; return this; }
        public Builder appointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; return this; }
        public Builder appointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; return this; }
        public Builder queueNumber(int queueNumber) { this.queueNumber = queueNumber; return this; }
        public Builder status(Status status) { this.status = status; return this; }
        public Builder cancelReason(String cancelReason) { this.cancelReason = cancelReason; return this; }

        public Appointment build() {
            Appointment a = new Appointment();
            a.id = this.id;
            a.patient = this.patient;
            a.doctor = this.doctor;
            a.appointmentDate = this.appointmentDate;
            a.appointmentTime = this.appointmentTime;
            a.queueNumber = this.queueNumber;
            a.status = this.status;
            a.cancelReason = this.cancelReason;
            return a;
        }
    }

    // Getters
    public Long getId() { return id; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public LocalTime getAppointmentTime() { return appointmentTime; }
    public int getQueueNumber() { return queueNumber; }
    public Status getStatus() { return status; }
    public String getCancelReason() { return cancelReason; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }
    public void setQueueNumber(int queueNumber) { this.queueNumber = queueNumber; }
    public void setStatus(Status status) { this.status = status; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
}
