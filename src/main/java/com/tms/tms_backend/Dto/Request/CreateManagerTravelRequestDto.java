package com.tms.tms_backend.Dto.Request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

    @Data
    public class CreateManagerTravelRequestDto {

        private Long managerId;

        private String destination;

        private String purpose;

        private LocalDate startDate;

        private LocalDate endDate;

        private String transportMode;

        private Boolean hotelRequired;

        private String hotelPreference;

        private BigDecimal estimatedBudget;

        private Boolean advancePayment;

        private String additionalNotes;
    }

