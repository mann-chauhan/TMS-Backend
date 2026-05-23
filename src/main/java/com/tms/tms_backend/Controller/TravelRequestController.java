package com.tms.tms_backend.Controller;

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

    private final TravelRequestService travelRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<TravelRequestResponse>>
    createRequest(
            @RequestBody CreateTravelRequestDto dto
    ) {

        TravelRequestResponse response =
                travelRequestService.createRequest(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                response,
                                "Travel Request Created Successfully"
                        )
                );
    }

    @GetMapping("/employee/{employeeId}")
    public ApiResponse<List<TravelRequestResponse>>
    getRequestsByEmployee(
            @PathVariable Long employeeId
    ) {

        return ApiResponse.success(

                travelRequestService
                        .getRequestsByEmployee(employeeId),

                "Employee Requests Fetched Successfully"
        );
    }

    @PutMapping("/cancel/{id}")
    public ApiResponse cancelRequest(
            @PathVariable Long id){
        return ApiResponse.builder()
                .success(true)
                .message("Request Cancelled Successfully")
                .data(travelRequestService.cancelRequest(id))
                .build();
    }

    @GetMapping("/manager/{managerId}")

    public ApiResponse getManagerRequests(
            @PathVariable Long managerId
    ){

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

    @PutMapping("/approve/{id}")

    public ApiResponse approveRequest(
            @PathVariable Long id
    ){

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

    @PutMapping("/reject/{id}")

    public ApiResponse rejectRequest(
            @PathVariable Long id
    ){

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
}