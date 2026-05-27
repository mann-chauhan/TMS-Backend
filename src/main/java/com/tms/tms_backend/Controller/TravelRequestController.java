package com.tms.tms_backend.Controller;
import com.tms.tms_backend.Dto.Request.CreateManagerTravelRequestDto;
import com.tms.tms_backend.Dto.Request.CreateTravelRequestDto;
import com.tms.tms_backend.Dto.Response.ApiResponse;
import com.tms.tms_backend.Dto.Response.TravelRequestResponse;
import com.tms.tms_backend.Service.TravelRequestService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class TravelRequestController {

    private final TravelRequestService
            travelRequestService;

    // =========================================
    // CREATE REQUEST
    // =========================================

    @PostMapping
    public ResponseEntity<ApiResponse<TravelRequestResponse>>
    createRequest(
            @RequestBody CreateTravelRequestDto dto
    ) {

        TravelRequestResponse response =
                travelRequestService
                        .createRequest(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                response,
                                "Travel Request Created Successfully"
                        )
                );
    }

    // =========================================
    // EMPLOYEE REQUESTS
    // =========================================

    @GetMapping("/employee/{employeeId}")
    public ApiResponse<List<TravelRequestResponse>>
    getRequestsByEmployee(
            @PathVariable Long employeeId
    ) {

        return ApiResponse.success(

                travelRequestService
                        .getRequestsByEmployee(
                                employeeId
                        ),

                "Employee Requests Fetched Successfully"
        );
    }

    // =========================================
    // CANCEL REQUEST
    // =========================================

    @PutMapping("/cancel/{id}")
    public ApiResponse cancelRequest(
            @PathVariable Long id
    ) {

        return ApiResponse.builder()

                .success(true)

                .message(
                        "Request Cancelled Successfully"
                )

                .data(
                        travelRequestService
                                .cancelRequest(id)
                )

                .build();
    }

    // =========================================
    // MANAGER REQUESTS
    // =========================================

    @GetMapping("/manager/{managerId}")
    public ApiResponse getManagerRequests(
            @PathVariable Long managerId
    ) {

        return ApiResponse.builder()

                .success(true)

                .message(
                        "Manager Requests Fetched Successfully"
                )

                .data(
                        travelRequestService
                                .getRequestsByManager(
                                        managerId
                                )
                )

                .build();
    }

    // =========================================
    // MANAGER APPROVE
    // =========================================

    @PutMapping("/approve/{id}")
    public ApiResponse approveRequest(
            @PathVariable Long id
    ) {

        return ApiResponse.builder()

                .success(true)

                .message(
                        "Request Approved Successfully"
                )

                .data(
                        travelRequestService
                                .approveRequest(id)
                )

                .build();
    }

    // =========================================
    // MANAGER REJECT
    // =========================================

    @PutMapping("/reject/{id}")
    public ApiResponse rejectRequest(
            @PathVariable Long id
    ) {

        return ApiResponse.builder()

                .success(true)

                .message(
                        "Request Rejected Successfully"
                )

                .data(
                        travelRequestService
                                .rejectRequest(id)
                )

                .build();
    }

    // =========================================
    // FINANCE REQUESTS
    // =========================================

    @GetMapping("/finance")
    public ApiResponse getFinanceRequests() {

        return ApiResponse.builder()

                .success(true)

                .message(
                        "Finance Requests Fetched Successfully"
                )

                .data(
                        travelRequestService
                                .getFinanceRequests()
                )

                .build();
    }

    // =========================================
    // FINANCE APPROVE
    // =========================================

    @PutMapping("/finance/approve/{id}")
    public ApiResponse financeApprove(
            @PathVariable Long id
    ) {

        return ApiResponse.builder()

                .success(true)

                .message(
                        "Finance Approved Successfully"
                )

                .data(
                        travelRequestService
                                .financeApproveRequest(id)
                )

                .build();
    }

    // =========================================
    // FINANCE REJECT
    // =========================================

    @PutMapping("/finance/reject/{id}")
    public ApiResponse financeReject(
            @PathVariable Long id
    ) {

        return ApiResponse.builder()

                .success(true)

                .message(
                        "Finance Rejected Successfully"
                )

                .data(
                        travelRequestService
                                .financeRejectRequest(id)
                )

                .build();
    }

    @GetMapping("/finance/history")
    public ApiResponse getFinanceDecisionHistory() {

        return ApiResponse.builder()
                .success(true)
                .message("Finance decision history fetched")
                .data(
                        travelRequestService
                                .getFinanceDecisionHistory()
                )
                .build();
    }

    @PostMapping("/manager/create")
    public ApiResponse createManagerRequest(
            @RequestBody
            CreateManagerTravelRequestDto dto
    ) {

        return ApiResponse.builder()
                .success(true)
                .message("Manager request created successfully")
                .data(
                        travelRequestService
                                .createManagerRequest(dto)
                )
                .build();
    }

    @GetMapping("/manager/history/{managerId}")
    public ApiResponse getManagerRequestHistory(
            @PathVariable Long managerId
    ) {

        return ApiResponse.builder()

                .success(true)

                .message(
                        "Manager Request History Fetched"
                )

                .data(
                        travelRequestService
                                .getManagerRequestHistory(
                                        managerId
                                )
                )

                .build();
    }

}