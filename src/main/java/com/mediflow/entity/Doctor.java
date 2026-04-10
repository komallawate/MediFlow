package com.mediflow.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String department;

    private String qualification;
    private String phone;
    private int avgConsultationMinutes = 15;
    private boolean available = true;

    // Constructors
    public Doctor() {}

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private String department;
        private String qualification;
        private String phone;
        private int avgConsultationMinutes = 15;
        private boolean available = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder department(String department) { this.department = department; return this; }
        public Builder qualification(String qualification) { this.qualification = qualification; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder avgConsultationMinutes(int avgConsultationMinutes) { this.avgConsultationMinutes = avgConsultationMinutes; return this; }
        public Builder available(boolean available) { this.available = available; return this; }

        public Doctor build() {
            Doctor d = new Doctor();
            d.id = this.id;
            d.name = this.name;
            d.department = this.department;
            d.qualification = this.qualification;
            d.phone = this.phone;
            d.avgConsultationMinutes = this.avgConsultationMinutes;
            d.available = this.available;
            return d;
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public String getQualification() { return qualification; }
    public String getPhone() { return phone; }
    public int getAvgConsultationMinutes() { return avgConsultationMinutes; }
    public boolean isAvailable() { return available; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDepartment(String department) { this.department = department; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAvgConsultationMinutes(int avgConsultationMinutes) { this.avgConsultationMinutes = avgConsultationMinutes; }
    public void setAvailable(boolean available) { this.available = available; }
}
