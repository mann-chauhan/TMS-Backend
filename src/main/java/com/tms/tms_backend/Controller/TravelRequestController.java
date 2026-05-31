package com.tms.tms_backend.Controller;

import com.tms.tms_backend.Dto.Request.CreateManagerTravelRequestDto;
import com.tms.tms_backend.Dto.Request.CreateTravelRequestDto;
import com.tms.tms_backend.Dto.Request.FinanceDecisionDto;
import com.tms.tms_backend.Dto.Response.ApiResponse;
import com.tms.tms_backend.Dto.Response.TravelRequestResponse;
import com.tms.tms_backend.Repository.UserRepository;
import com.tms.tms_backend.Service.TravelRequestService;
import com.tms.tms_backend.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class TravelRequestController {

    private static final Logger log =
            LoggerFactory.getLogger(TravelRequestController.class);

    private final TravelRequestService
            travelRequestService;

    // Used only to extract logged-in userId from the existing JWT (no auth redesign).
    private final JwtService jwtService;

    private final UserRepository userRepository;

    // =========================================
    // CREATE REQUEST
    // =========================================

    @PostMapping
    public ResponseEntity<ApiResponse<TravelRequestResponse>>
    createRequest(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CreateTravelRequestDto dto
    ) {

        // Always trust employee identity from JWT, not from request body.
        // This also avoids issues when DB is reset but frontend still sends a stale/incorrect employeeId.
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);
        Long userIdFromJwt = jwtService.extractUserId(token);
        String emailFromJwt = jwtService.extractEmail(token);

        Long resolvedEmployeeId =
                userRepository
                        .findByEmail(emailFromJwt)
                        .orElseThrow(() ->
                                new RuntimeException("Employee not found")
                        )
                        .getId();

        log.info(
                "Create travel request: jwtUserId={}, jwtEmail={}, resolvedEmployeeId={}, bodyEmployeeId={}",
                userIdFromJwt,
                emailFromJwt,
                resolvedEmployeeId,
                dto.getEmployeeId()
        );

        // Prefer email-based lookup to avoid stale numeric userId issues after DB resets.
        dto.setEmployeeId(resolvedEmployeeId);

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
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long employeeId
    ) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);
        String emailFromJwt = jwtService.extractEmail(token);
        Long resolvedEmployeeId =
                userRepository
                        .findByEmail(emailFromJwt)
                        .orElseThrow(() ->
                                new RuntimeException("Employee not found")
                        )
                        .getId();

        // Helps detect hardcoded/stale ids on the frontend.
        if (!resolvedEmployeeId.equals(employeeId)) {
            log.info(
                    "My requests: body/path employeeId={} does not match JWT employeeId={} (email={})",
                    employeeId,
                    resolvedEmployeeId,
                    emailFromJwt
            );
        }

        return ApiResponse.success(

                travelRequestService
                        .getRequestsByEmployee(
                                resolvedEmployeeId
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

    @GetMapping("/manager")
    public ApiResponse getManagerRequests(
            @RequestHeader("Authorization") String authorizationHeader
    ) {

        // Manager id must come from JWT so a manager cannot query another manager's requests.
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);
        Long managerIdFromJwt = jwtService.extractUserId(token);
        String emailFromJwt = jwtService.extractEmail(token);

        Long resolvedManagerId =
                userRepository
                        .findByEmail(emailFromJwt)
                        .orElseThrow(() ->
                                new RuntimeException("Manager not found")
                        )
                        .getId();

        log.info(
                "Manager requests: jwtUserId={}, jwtEmail={}, resolvedManagerId={}",
                managerIdFromJwt,
                emailFromJwt,
                resolvedManagerId
        );

        return ApiResponse.builder()

                .success(true)

                .message(
                        "Manager Requests Fetched Successfully"
                )

                .data(
                        travelRequestService
                                .getRequestsByManager(
                                        resolvedManagerId
                                )
                )

                .build();
    }

    // Backward-compatible endpoint: some frontends still call /manager/{managerId}.
    // We ignore the path id and always resolve manager from JWT.
    @GetMapping("/manager/{managerId}")
    public ApiResponse getManagerRequestsLegacy(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long managerId
    ) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);
        String emailFromJwt = jwtService.extractEmail(token);
        Long resolvedManagerId =
                userRepository
                        .findByEmail(emailFromJwt)
                        .orElseThrow(() ->
                                new RuntimeException("Manager not found")
                        )
                        .getId();

        if (!resolvedManagerId.equals(managerId)) {
            log.info(
                    "Manager requests (legacy): path managerId={} does not match JWT managerId={} (email={})",
                    managerId,
                    resolvedManagerId,
                    emailFromJwt
            );
        }

        return ApiResponse.builder()
                .success(true)
                .message("Manager Requests Fetched Successfully")
                .data(
                        travelRequestService.getRequestsByManager(resolvedManagerId)
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
            @PathVariable Long id,
            @RequestBody FinanceDecisionDto dto
    ) {

        return ApiResponse.builder()

                .success(true)

                .message(
                        "Finance Approved Successfully"
                )

                .data(
                        travelRequestService
                                .financeApproveRequest(
                                        id,
                                        dto.getRemarks()
                                )
                )

                .build();
    }

    // =========================================
    // FINANCE REJECT
    // =========================================

    @PutMapping("/finance/reject/{id}")
    public ApiResponse financeReject(
            @PathVariable Long id,
            @RequestBody FinanceDecisionDto dto
    ) {

        return ApiResponse.builder()

                .success(true)

                .message(
                        "Finance Rejected Successfully"
                )

                .data(
                        travelRequestService
                                .financeRejectRequest(id,dto.getRemarks())
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
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody
            CreateManagerTravelRequestDto dto
    ) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);
        String emailFromJwt = jwtService.extractEmail(token);
        Long resolvedManagerId =
                userRepository
                        .findByEmail(emailFromJwt)
                        .orElseThrow(() ->
                                new RuntimeException("Manager not found")
                        )
                        .getId();

        dto.setManagerId(resolvedManagerId);

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
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long managerId
    ) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);
        String emailFromJwt = jwtService.extractEmail(token);
        Long resolvedManagerId =
                userRepository
                        .findByEmail(emailFromJwt)
                        .orElseThrow(() ->
                                new RuntimeException("Manager not found")
                        )
                        .getId();

        if (!resolvedManagerId.equals(managerId)) {
            log.info(
                    "Manager history: path managerId={} does not match JWT managerId={} (email={})",
                    managerId,
                    resolvedManagerId,
                    emailFromJwt
            );
        }

        return ApiResponse.builder()

                .success(true)

                .message(
                        "Manager Request History Fetched"
                )

                .data(
                        travelRequestService
                                .getManagerRequestHistory(
                                        resolvedManagerId
                                )
                )

                .build();
    }

}
