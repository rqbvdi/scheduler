package com.example.schedule.entity;

import com.example.schedule.entity.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "leave_request")
public class LeaveRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false)
    private ZonedDateTime start;

    @Column(nullable = false)
    private ZonedDateTime end;

    private String reason;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    private Long approvedBy;

    private ZonedDateTime approvedAt;

    @CreationTimestamp
    private ZonedDateTime createdAt;
}
