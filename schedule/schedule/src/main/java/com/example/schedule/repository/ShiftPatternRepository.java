package com.example.schedule.repository;

import com.example.schedule.entity.ShiftPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftPatternRepository extends JpaRepository<ShiftPattern, Long> {

}
