package com.mediflow.service;

import com.mediflow.dto.AppointmentDto;
import com.mediflow.entity.Appointment;
import com.mediflow.entity.Doctor;
import com.mediflow.entity.Patient;
import com.mediflow.repository.AppointmentRepository;
import com.mediflow.repository.DoctorRepository;
import com.mediflow.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository apptRepo;
    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;

    public AppointmentService(AppointmentRepository apptRepo,
                               PatientRepository patientRepo,
                               DoctorRepository doctorRepo) {
        this.apptRepo = apptRepo;
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
    }

    public AppointmentDto.AppointmentResponse book(AppointmentDto.BookRequest req) {
        Patient patient = patientRepo.findById(req.getPatientId())
            .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepo.findById(req.getDoctorId())
            .orElseThrow(() -> new RuntimeException("Doctor not found"));

        int queueNum = apptRepo.findMaxQueueNumber(req.getDoctorId(), req.getAppointmentDate()) + 1;

        Appointment appt = Appointment.builder()
            .patient(patient)
            .doctor(doctor)
            .appointmentDate(req.getAppointmentDate())
            .appointmentTime(req.getAppointmentTime())
            .queueNumber(queueNum)
            .status(Appointment.Status.BOOKED)
            .build();

        return toResponse(apptRepo.save(appt));
    }

    public List<AppointmentDto.QueueEntry> getQueueForDoctor(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepo.findById(doctorId)
            .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<Appointment> appointments =
            apptRepo.findByDoctor_IdAndAppointmentDateOrderByQueueNumber(doctorId, date);

        List<AppointmentDto.QueueEntry> queue = new ArrayList<>();

        for (Appointment a : appointments) {
            AppointmentDto.QueueEntry entry = new AppointmentDto.QueueEntry();
            entry.setAppointmentId(a.getId());
            entry.setQueueNumber(a.getQueueNumber());
            entry.setPatientName(a.getPatient().getName());
            entry.setPatientCode(a.getPatient().getPatientCode());
            entry.setStatus(a.getStatus().name());
            entry.setAppointmentTime(a.getAppointmentTime());
            entry.setTokenDisplay("MF-" + String.format("%03d", a.getQueueNumber()));

            if (a.getStatus() == Appointment.Status.COMPLETED
                    || a.getStatus() == Appointment.Status.CANCELLED) {
                entry.setEstimatedWaitMinutes(0);
            } else if (a.getStatus() == Appointment.Status.IN_PROGRESS) {
                entry.setEstimatedWaitMinutes(doctor.getAvgConsultationMinutes() / 2);
            } else {
                long ahead = appointments.stream()
                    .filter(x -> x.getQueueNumber() < a.getQueueNumber()
                        && (x.getStatus() == Appointment.Status.BOOKED
                            || x.getStatus() == Appointment.Status.IN_PROGRESS))
                    .count();
                entry.setEstimatedWaitMinutes((int) (ahead * doctor.getAvgConsultationMinutes()));
            }

            queue.add(entry);
        }
        return queue;
    }

    public List<AppointmentDto.DepartmentQueueSummary> getDeptSummaries() {
        LocalDate today = LocalDate.now();
        List<Doctor> doctors = doctorRepo.findAll();
        List<AppointmentDto.DepartmentQueueSummary> summaries = new ArrayList<>();

        for (Doctor d : doctors) {
            List<Appointment> todayAppts =
                apptRepo.findByDoctor_IdAndAppointmentDateOrderByQueueNumber(d.getId(), today);

            if (todayAppts.isEmpty()) continue;

            long waiting   = todayAppts.stream().filter(a -> a.getStatus() == Appointment.Status.BOOKED).count();
            long inProgress= todayAppts.stream().filter(a -> a.getStatus() == Appointment.Status.IN_PROGRESS).count();
            long completed = todayAppts.stream().filter(a -> a.getStatus() == Appointment.Status.COMPLETED).count();

            AppointmentDto.DepartmentQueueSummary summary = new AppointmentDto.DepartmentQueueSummary();
            summary.setDepartment(d.getDepartment());
            summary.setDoctorName(d.getName());
            summary.setWaiting((int) waiting);
            summary.setInProgress((int) inProgress);
            summary.setCompleted((int) completed);
            summary.setEstimatedWaitForNext((int) (waiting * d.getAvgConsultationMinutes()));
            summaries.add(summary);
        }
        return summaries;
    }

    public List<AppointmentDto.SlotInfo> getAvailableSlots(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepo.findById(doctorId)
            .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<Appointment> booked =
            apptRepo.findByDoctor_IdAndAppointmentDateOrderByQueueNumber(doctorId, date);

        List<AppointmentDto.SlotInfo> slots = new ArrayList<>();
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end   = LocalTime.of(17, 0);
        int slotMinutes = doctor.getAvgConsultationMinutes();
        int position = 1;

        while (start.isBefore(end)) {
            final LocalTime slotTime = start;
            boolean taken = booked.stream()
                .anyMatch(a -> a.getAppointmentTime().equals(slotTime)
                    && a.getStatus() != Appointment.Status.CANCELLED);

            AppointmentDto.SlotInfo slot = new AppointmentDto.SlotInfo();
            slot.setTime(slotTime);
            slot.setAvailable(!taken);
            slot.setQueuePosition(position++);
            slots.add(slot);

            start = start.plusMinutes(slotMinutes);
        }
        return slots;
    }

    public AppointmentDto.AppointmentResponse updateStatus(Long id, Appointment.Status status) {
        Appointment a = apptRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        a.setStatus(status);
        return toResponse(apptRepo.save(a));
    }

    public AppointmentDto.AppointmentResponse cancel(Long id, String reason) {
        Appointment a = apptRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        a.setStatus(Appointment.Status.CANCELLED);
        a.setCancelReason(reason);
        return toResponse(apptRepo.save(a));
    }

    public List<AppointmentDto.AppointmentResponse> getByPatient(Long patientId) {
        return apptRepo.findByPatient_IdOrderByAppointmentDateDescAppointmentTimeDesc(patientId)
            .stream().map(this::toResponse).toList();
    }

    private AppointmentDto.AppointmentResponse toResponse(Appointment a) {
        AppointmentDto.AppointmentResponse r = new AppointmentDto.AppointmentResponse();
        r.setId(a.getId());
        r.setPatientName(a.getPatient().getName());
        r.setPatientCode(a.getPatient().getPatientCode());
        r.setDoctorName(a.getDoctor().getName());
        r.setDepartment(a.getDoctor().getDepartment());
        r.setAppointmentDate(a.getAppointmentDate());
        r.setAppointmentTime(a.getAppointmentTime());
        r.setQueueNumber(a.getQueueNumber());
        r.setStatus(a.getStatus().name());
        return r;
    }
}
