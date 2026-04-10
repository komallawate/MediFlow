package com.mediflow.repository;

import com.mediflow.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatient_IdOrderByIssuedDateDesc(Long patientId);
}
