package com.mediflow.controller;

import com.mediflow.entity.Patient;
import com.mediflow.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientRepository patientRepo;

    public PatientController(PatientRepository patientRepo) {
        this.patientRepo = patientRepo;
    }

    @PostMapping
    public ResponseEntity<Patient> create(@RequestBody Patient patient) {
        Patient saved = patientRepo.save(patient);
        saved.setPatientCode(String.format("MF-%05d", saved.getId()));
        return ResponseEntity.ok(patientRepo.save(saved));
    }

    @GetMapping
    public ResponseEntity<Page<Patient>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String q) {
        PageRequest pageable = PageRequest.of(page, 10);
        if (q.isBlank()) return ResponseEntity.ok(patientRepo.findAll(pageable));
        return ResponseEntity.ok(patientRepo.search(q, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> get(@PathVariable Long id) {
        return patientRepo.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Patient> getByCode(@PathVariable String code) {
        return patientRepo.findByPatientCode(code)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> update(@PathVariable Long id, @RequestBody Patient body) {
        return patientRepo.findById(id).map(p -> {
            p.setName(body.getName());
            p.setPhone(body.getPhone());
            p.setEmail(body.getEmail());
            p.setAddress(body.getAddress());
            return ResponseEntity.ok(patientRepo.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }
}
