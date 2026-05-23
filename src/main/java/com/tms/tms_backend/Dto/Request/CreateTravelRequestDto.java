package com.tms.tms_backend.Dto.Request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateTravelRequestDto {

    private Long employeeId;

    private String fromLocation;

    private String destination;

    private String purpose;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal estimatedBudget;

    private String transportMode;

    private Boolean hotelRequired;

    private String hotelPreference;

    private Boolean advancePayment;

    private String additionalNotes;
}