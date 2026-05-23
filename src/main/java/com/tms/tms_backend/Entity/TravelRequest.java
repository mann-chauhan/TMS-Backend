package com.tms.tms_backend.Entity;

import com.tms.tms_backend.Enum.TravelRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "travel_requests")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TravelRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestCode;

    private String fromLocation;

    private String destination;

    @Column(columnDefinition = "TEXT")
    private String purpose;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal estimatedBudget;

    private String transportMode;

    private Boolean hotelRequired;

    private String hotelPreference;

    private Boolean advancePayment;

    @Column(length = 1000)
    private String additionalNotes;

    // EMPLOYEE

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    // MANAGER

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    // STATUS

    @Enumerated(EnumType.STRING)
    private TravelRequestStatus status;
}