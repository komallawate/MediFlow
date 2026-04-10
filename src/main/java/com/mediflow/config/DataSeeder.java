package com.mediflow.config;

import com.mediflow.entity.Doctor;
import com.mediflow.entity.User;
import com.mediflow.repository.DoctorRepository;
import com.mediflow.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final DoctorRepository doctorRepo;
    private final PasswordEncoder encoder;

    public DataSeeder(UserRepository userRepo, DoctorRepository doctorRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.doctorRepo = doctorRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepo.existsByUsername("admin")) {
            userRepo.saveAll(List.of(
                User.builder().username("admin").password(encoder.encode("admin123")).role(User.Role.ADMIN).build(),
                User.builder().username("doctor1").password(encoder.encode("doctor123")).role(User.Role.DOCTOR).build(),
                User.builder().username("receptionist").password(encoder.encode("recept123")).role(User.Role.RECEPTIONIST).build()
            ));
        }
        if (doctorRepo.count() == 0) {
            doctorRepo.saveAll(List.of(
                Doctor.builder().name("Dr. Anjali Sharma").department("Cardiology").qualification("MD, DM Cardiology").phone("9876543210").avgConsultationMinutes(20).build(),
                Doctor.builder().name("Dr. Ravi Kulkarni").department("General Medicine").qualification("MBBS, MD").phone("9876543211").avgConsultationMinutes(15).build(),
                Doctor.builder().name("Dr. Priya Menon").department("Pediatrics").qualification("MBBS, DCH").phone("9876543212").avgConsultationMinutes(15).build(),
                Doctor.builder().name("Dr. Suresh Patil").department("Orthopedics").qualification("MS Ortho").phone("9876543213").avgConsultationMinutes(25).build(),
                Doctor.builder().name("Dr. Neha Joshi").department("Dermatology").qualification("MD Dermatology").phone("9876543214").avgConsultationMinutes(10).build()
            ));
        }
    }
}
