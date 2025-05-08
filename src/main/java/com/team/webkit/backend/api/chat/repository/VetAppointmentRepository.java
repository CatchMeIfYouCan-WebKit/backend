package com.team.webkit.backend.api.chat.repository;

import com.team.webkit.backend.api.chat.entity.VetAppointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VetAppointmentRepository extends JpaRepository<VetAppointment, Long> {
}
