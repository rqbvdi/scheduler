package com.example.schedule.repository;

import com.example.schedule.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByStartBetween(LocalDateTime start, LocalDateTime end); // for calendar view
}
