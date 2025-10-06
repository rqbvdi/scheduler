package com.example.schedule.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "shift_patterns")
public class ShiftPattern {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g., "5/2", "2/2"

    @Column(nullable = false)
    private Integer workDays;

    @Column(nullable = false)
    private Integer restDays;

    @Column(nullable = false)
    private LocalTime defaultStart;

    @Column(nullable = false)
    private LocalTime defaultEnd;

    @OneToMany(mappedBy = "shiftPattern", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Shift> shifts = new HashSet<>();
}
