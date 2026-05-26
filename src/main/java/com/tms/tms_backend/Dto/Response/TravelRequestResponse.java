package com.tms.tms_backend.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelRequestResponse {

    private Long id;

    private String requestCode;

    private String employeeName;

    private String department;

    private String managerName;

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

    private String status;
}