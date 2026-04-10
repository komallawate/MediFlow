package com.mediflow.controller;

import com.mediflow.entity.Doctor;
import com.mediflow.repository.DoctorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorRepository doctorRepo;

    public DoctorController(DoctorRepository doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    @GetMapping
    public List<Doctor> list() {
        return doctorRepo.findAll();
    }

    @GetMapping("/available")
    public List<Doctor> available() {
        return doctorRepo.findByAvailableTrue();
    }

    @GetMapping("/department/{dept}")
    public List<Doctor> byDept(@PathVariable String dept) {
        return doctorRepo.findByDepartment(dept);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> get(@PathVariable Long id) {
        return doctorRepo.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Doctor> create(@RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorRepo.save(doctor));
    }

    @PutMapping("/{id}/toggle-availability")
    public ResponseEntity<Doctor> toggle(@PathVariable Long id) {
        return doctorRepo.findById(id).map(d -> {
            d.setAvailable(!d.isAvailable());
            return ResponseEntity.ok(doctorRepo.save(d));
        }).orElse(ResponseEntity.notFound().build());
    }
}
