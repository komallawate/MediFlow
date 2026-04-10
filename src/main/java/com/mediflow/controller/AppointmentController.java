package com.mediflow.controller;

import com.mediflow.dto.AppointmentDto;
import com.mediflow.entity.Appointment;
import com.mediflow.repository.AppointmentRepository;
import com.mediflow.repository.DoctorRepository;
import com.mediflow.repository.PatientRepository;
import com.mediflow.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService apptService;
    private final AppointmentRepository apptRepo;
    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;

    public AppointmentController(AppointmentService apptService,
                                  AppointmentRepository apptRepo,
                                  PatientRepository patientRepo,
                                  DoctorRepository doctorRepo) {
        this.apptService = apptService;
        this.apptRepo = apptRepo;
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
    }

    @PostMapping
    public ResponseEntity<AppointmentDto.AppointmentResponse> book(
            @RequestBody AppointmentDto.BookRequest req) {
        return ResponseEntity.ok(apptService.book(req));
    }

    @GetMapping("/patient/{patientId}")
    public List<AppointmentDto.AppointmentResponse> byPatient(@PathVariable Long patientId) {
        return apptService.getByPatient(patientId);
    }

    @GetMapping("/doctor/{doctorId}/queue")
    public List<AppointmentDto.QueueEntry> doctorQueue(
            @PathVariable Long doctorId,
            @RequestParam(required = false) String date) {
        LocalDate d = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        return apptService.getQueueForDoctor(doctorId, d);
    }

    @GetMapping("/doctor/{doctorId}/slots")
    public List<AppointmentDto.SlotInfo> slots(
            @PathVariable Long doctorId,
            @RequestParam String date) {
        return apptService.getAvailableSlots(doctorId, LocalDate.parse(date));
    }

    @GetMapping("/queue/summary")
    public List<AppointmentDto.DepartmentQueueSummary> queueSummary() {
        return apptService.getDeptSummaries();
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<AppointmentDto.AppointmentResponse> start(@PathVariable Long id) {
        return ResponseEntity.ok(apptService.updateStatus(id, Appointment.Status.IN_PROGRESS));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<AppointmentDto.AppointmentResponse> complete(@PathVariable Long id) {
        return ResponseEntity.ok(apptService.updateStatus(id, Appointment.Status.COMPLETED));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentDto.AppointmentResponse> cancel(
            @PathVariable Long id,
            @RequestBody AppointmentDto.CancelRequest req) {
        return ResponseEntity.ok(apptService.cancel(id, req.getReason()));
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<AppointmentDto.DashboardStats> stats() {
        AppointmentDto.DashboardStats stats = new AppointmentDto.DashboardStats();
        stats.setTodayAppointments(apptRepo.countByDate(LocalDate.now()));
        stats.setTotalPatients(patientRepo.count());
        stats.setCompletedToday(apptRepo.countCompleted());
        stats.setTotalDoctors(doctorRepo.count());
        stats.setDepartmentSummaries(apptService.getDeptSummaries());
        return ResponseEntity.ok(stats);
    }
}
