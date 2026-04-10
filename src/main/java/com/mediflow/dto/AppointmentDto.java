package com.mediflow.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentDto {

    public static class BookRequest {
        private Long patientId;
        private Long doctorId;
        private LocalDate appointmentDate;
        private LocalTime appointmentTime;

        public Long getPatientId() { return patientId; }
        public void setPatientId(Long patientId) { this.patientId = patientId; }
        public Long getDoctorId() { return doctorId; }
        public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
        public LocalDate getAppointmentDate() { return appointmentDate; }
        public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
        public LocalTime getAppointmentTime() { return appointmentTime; }
        public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }
    }

    public static class CancelRequest {
        private String reason;

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    public static class AppointmentResponse {
        private Long id;
        private String patientName;
        private String patientCode;
        private String doctorName;
        private String department;
        private LocalDate appointmentDate;
        private LocalTime appointmentTime;
        private int queueNumber;
        private String status;
        private int estimatedWaitMinutes;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getPatientName() { return patientName; }
        public void setPatientName(String patientName) { this.patientName = patientName; }
        public String getPatientCode() { return patientCode; }
        public void setPatientCode(String patientCode) { this.patientCode = patientCode; }
        public String getDoctorName() { return doctorName; }
        public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public LocalDate getAppointmentDate() { return appointmentDate; }
        public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
        public LocalTime getAppointmentTime() { return appointmentTime; }
        public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }
        public int getQueueNumber() { return queueNumber; }
        public void setQueueNumber(int queueNumber) { this.queueNumber = queueNumber; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getEstimatedWaitMinutes() { return estimatedWaitMinutes; }
        public void setEstimatedWaitMinutes(int estimatedWaitMinutes) { this.estimatedWaitMinutes = estimatedWaitMinutes; }
    }

    public static class QueueEntry {
        private Long appointmentId;
        private int queueNumber;
        private String patientName;
        private String patientCode;
        private String status;
        private LocalTime appointmentTime;
        private int estimatedWaitMinutes;
        private String tokenDisplay;

        public Long getAppointmentId() { return appointmentId; }
        public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
        public int getQueueNumber() { return queueNumber; }
        public void setQueueNumber(int queueNumber) { this.queueNumber = queueNumber; }
        public String getPatientName() { return patientName; }
        public void setPatientName(String patientName) { this.patientName = patientName; }
        public String getPatientCode() { return patientCode; }
        public void setPatientCode(String patientCode) { this.patientCode = patientCode; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public LocalTime getAppointmentTime() { return appointmentTime; }
        public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }
        public int getEstimatedWaitMinutes() { return estimatedWaitMinutes; }
        public void setEstimatedWaitMinutes(int estimatedWaitMinutes) { this.estimatedWaitMinutes = estimatedWaitMinutes; }
        public String getTokenDisplay() { return tokenDisplay; }
        public void setTokenDisplay(String tokenDisplay) { this.tokenDisplay = tokenDisplay; }
    }

    public static class DepartmentQueueSummary {
        private String department;
        private String doctorName;
        private int waiting;
        private int inProgress;
        private int completed;
        private int estimatedWaitForNext;

        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public String getDoctorName() { return doctorName; }
        public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
        public int getWaiting() { return waiting; }
        public void setWaiting(int waiting) { this.waiting = waiting; }
        public int getInProgress() { return inProgress; }
        public void setInProgress(int inProgress) { this.inProgress = inProgress; }
        public int getCompleted() { return completed; }
        public void setCompleted(int completed) { this.completed = completed; }
        public int getEstimatedWaitForNext() { return estimatedWaitForNext; }
        public void setEstimatedWaitForNext(int estimatedWaitForNext) { this.estimatedWaitForNext = estimatedWaitForNext; }
    }

    public static class SlotInfo {
        private LocalTime time;
        private boolean available;
        private int queuePosition;

        public LocalTime getTime() { return time; }
        public void setTime(LocalTime time) { this.time = time; }
        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }
        public int getQueuePosition() { return queuePosition; }
        public void setQueuePosition(int queuePosition) { this.queuePosition = queuePosition; }
    }

    public static class DashboardStats {
        private long todayAppointments;
        private long totalPatients;
        private long completedToday;
        private long totalDoctors;
        private List<DepartmentQueueSummary> departmentSummaries;

        public long getTodayAppointments() { return todayAppointments; }
        public void setTodayAppointments(long todayAppointments) { this.todayAppointments = todayAppointments; }
        public long getTotalPatients() { return totalPatients; }
        public void setTotalPatients(long totalPatients) { this.totalPatients = totalPatients; }
        public long getCompletedToday() { return completedToday; }
        public void setCompletedToday(long completedToday) { this.completedToday = completedToday; }
        public long getTotalDoctors() { return totalDoctors; }
        public void setTotalDoctors(long totalDoctors) { this.totalDoctors = totalDoctors; }
        public List<DepartmentQueueSummary> getDepartmentSummaries() { return departmentSummaries; }
        public void setDepartmentSummaries(List<DepartmentQueueSummary> departmentSummaries) { this.departmentSummaries = departmentSummaries; }
    }
}
