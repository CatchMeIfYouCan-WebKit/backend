package com.team.webkit.backend.api.appointment.repository;

import com.team.webkit.backend.api.appointment.entity.Appointment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByUserId(Integer userId);

    List<Appointment> findByVetId(Integer vetId);

    int countByUserIdAndVetId(Integer userId, Integer vetId);
}
