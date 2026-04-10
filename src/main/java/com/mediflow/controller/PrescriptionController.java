package com.mediflow.controller;

import com.mediflow.entity.Prescription;
import com.mediflow.repository.AppointmentRepository;
import com.mediflow.repository.DoctorRepository;
import com.mediflow.repository.PatientRepository;
import com.mediflow.repository.PrescriptionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionRepository prescriptionRepo;
    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;
    private final AppointmentRepository apptRepo;

    public PrescriptionController(PrescriptionRepository prescriptionRepo,
                                   PatientRepository patientRepo,
                                   DoctorRepository doctorRepo,
                                   AppointmentRepository apptRepo) {
        this.prescriptionRepo = prescriptionRepo;
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
        this.apptRepo = apptRepo;
    }

    static class PrescriptionRequest {
        private Long patientId;
        private Long doctorId;
        private Long appointmentId;
        private String diagnosis;
        private String medicinesJson;
        private String notes;

        public Long getPatientId() { return patientId; }
        public void setPatientId(Long patientId) { this.patientId = patientId; }
        public Long getDoctorId() { return doctorId; }
        public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
        public Long getAppointmentId() { return appointmentId; }
        public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
        public String getDiagnosis() { return diagnosis; }
        public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
        public String getMedicinesJson() { return medicinesJson; }
        public void setMedicinesJson(String medicinesJson) { this.medicinesJson = medicinesJson; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    @PostMapping
    public ResponseEntity<Prescription> issue(@RequestBody PrescriptionRequest req) {
        Prescription p = Prescription.builder()
            .patient(patientRepo.findById(req.getPatientId()).orElseThrow())
            .doctor(doctorRepo.findById(req.getDoctorId()).orElseThrow())
            .appointment(req.getAppointmentId() != null
                ? apptRepo.findById(req.getAppointmentId()).orElse(null) : null)
            .issuedDate(LocalDate.now())
            .diagnosis(req.getDiagnosis())
            .medicinesJson(req.getMedicinesJson())
            .notes(req.getNotes())
            .build();
        return ResponseEntity.ok(prescriptionRepo.save(p));
    }

    @GetMapping("/patient/{patientId}")
    public List<Prescription> history(@PathVariable Long patientId) {
        return prescriptionRepo.findByPatient_IdOrderByIssuedDateDesc(patientId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prescription> get(@PathVariable Long id) {
        return prescriptionRepo.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
