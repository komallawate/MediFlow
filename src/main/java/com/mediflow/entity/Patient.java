package com.mediflow.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    private String email;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String address;

    @Column(unique = true)
    private String patientCode;

    // Constructors
    public Patient() {}

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private String phone;
        private String email;
        private LocalDate dateOfBirth;
        private String gender;
        private String bloodGroup;
        private String address;
        private String patientCode;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder dateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; return this; }
        public Builder gender(String gender) { this.gender = gender; return this; }
        public Builder bloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; return this; }
        public Builder address(String address) { this.address = address; return this; }
        public Builder patientCode(String patientCode) { this.patientCode = patientCode; return this; }

        public Patient build() {
            Patient p = new Patient();
            p.id = this.id;
            p.name = this.name;
            p.phone = this.phone;
            p.email = this.email;
            p.dateOfBirth = this.dateOfBirth;
            p.gender = this.gender;
            p.bloodGroup = this.bloodGroup;
            p.address = this.address;
            p.patientCode = this.patientCode;
            return p;
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }
    public String getBloodGroup() { return bloodGroup; }
    public String getAddress() { return address; }
    public String getPatientCode() { return patientCode; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setGender(String gender) { this.gender = gender; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public void setAddress(String address) { this.address = address; }
    public void setPatientCode(String patientCode) { this.patientCode = patientCode; }
}
