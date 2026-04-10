package com.mediflow.repository;

import com.mediflow.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatient_IdOrderByAppointmentDateDescAppointmentTimeDesc(Long patientId);

    List<Appointment> findByDoctor_IdAndAppointmentDateOrderByQueueNumber(Long doctorId, LocalDate date);

    List<Appointment> findByDoctor_DepartmentAndAppointmentDateAndStatusNotOrderByQueueNumber(
            String department, LocalDate date, Appointment.Status status);

    @Query("SELECT COALESCE(MAX(a.queueNumber), 0) FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date")
    int findMaxQueueNumber(Long doctorId, LocalDate date);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.appointmentDate = :date AND a.status != 'CANCELLED'")
    long countByDate(LocalDate date);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.status = 'COMPLETED'")
    long countCompleted();

    Optional<Appointment> findTopByDoctor_IdAndAppointmentDateAndStatusOrderByQueueNumber(
            Long doctorId, LocalDate date, Appointment.Status status);
}
