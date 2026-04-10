package com.mediflow.repository;

import com.mediflow.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByPhone(String phone);
    Optional<Patient> findByPatientCode(String code);

    @Query("SELECT p FROM Patient p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%',:q,'%')) OR p.phone LIKE CONCAT('%',:q,'%')")
    Page<Patient> search(String q, Pageable pageable);
}
