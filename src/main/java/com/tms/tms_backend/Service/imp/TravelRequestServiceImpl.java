package com.tms.tms_backend.Service.imp;

import com.tms.tms_backend.Dto.Request.CreateManagerTravelRequestDto;
import com.tms.tms_backend.Dto.Request.CreateTravelRequestDto;
import com.tms.tms_backend.Dto.Response.TravelRequestResponse;
import com.tms.tms_backend.Entity.Role;
import com.tms.tms_backend.Entity.TravelRequest;
import com.tms.tms_backend.Entity.User;
import com.tms.tms_backend.Enum.TravelRequestStatus;
import com.tms.tms_backend.Repository.TravelRequestRepository;
import com.tms.tms_backend.Repository.UserRepository;
import com.tms.tms_backend.Service.TravelRequestService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelRequestServiceImpl
        implements TravelRequestService {

    private static final Logger log =
            LoggerFactory.getLogger(TravelRequestServiceImpl.class);

    private final TravelRequestRepository
            travelRequestRepository;

    private final UserRepository userRepository;

    @Override
    public TravelRequestResponse createRequest(
            CreateTravelRequestDto request
    ) {

        // EMPLOYEE

        User employee = userRepository.findById(
                request.getEmployeeId()
        ).orElseThrow(() ->
                new RuntimeException("Employee not found")
        );

        log.info(
                "Create travel request: employeeId={}, employeeName={}, employeeDepartment={}",
                employee.getId(),
                employee.getFullName(),
                employee.getDepartment()
        );

        String department = employee.getDepartment();
        if (department == null || department.trim().isEmpty()) {
            // This is the reason your manager lookup becomes:
            // where u1_0.department is null and r1_0.name = 'MANAGER'
            throw new RuntimeException(
                    "Employee department is missing for userId=" + employee.getId()
            );
        }

        // MANAGER

        List<User> managers =
                userRepository.findAllByDepartmentAndRole_Name(
                        department.trim(),
                        Role.RoleName.MANAGER
                );

        if (managers.isEmpty()) {
            throw new RuntimeException("Manager not found");
        }
        if (managers.size() > 1) {
            throw new RuntimeException(
                    "Duplicate managers found for department=" + department.trim()
            );
        }

        User manager = managers.getFirst();

        // CREATE REQUEST

        TravelRequest travelRequest =
                TravelRequest.builder()

                        .requestCode(
                                "TR-" + System.currentTimeMillis()
                        )

                        .fromLocation(
                                request.getFromLocation()
                        )

                        .destination(
                                request.getDestination()
                        )

                        .purpose(
                                request.getPurpose()
                        )

                        .startDate(
                                request.getStartDate()
                        )

                        .endDate(
                                request.getEndDate()
                        )

                        .estimatedBudget(
                                request.getEstimatedBudget()
                        )

                        .transportMode(
                                request.getTransportMode()
                        )

                        .hotelRequired(
                                request.getHotelRequired()
                        )

                        .hotelPreference(
                                request.getHotelPreference()
                        )

                        .advancePayment(
                                request.getAdvancePayment()
                        )

                        .additionalNotes(
                                request.getAdditionalNotes()
                        )

                        .employee(employee)

                        .manager(manager)

                        .status(
                                TravelRequestStatus.DRAFT
                        )

                        .build();

        TravelRequest savedRequest =
                travelRequestRepository.save(
                        travelRequest
                );

        // RESPONSE

        return TravelRequestResponse.builder()

                .id(savedRequest.getId())

                .requestCode(
                        savedRequest.getRequestCode()
                )

                .employeeName(
                        savedRequest
                                .getEmployee()
                                .getFullName()
                )

                .managerName(
                        savedRequest
                                .getManager()
                                .getFullName()
                )

                .fromLocation(
                        savedRequest.getFromLocation()
                )

                .destination(
                        savedRequest.getDestination()
                )

                .purpose(
                        savedRequest.getPurpose()
                )

                .startDate(
                        savedRequest.getStartDate()
                )

                .endDate(
                        savedRequest.getEndDate()
                )

                .estimatedBudget(
                        savedRequest.getEstimatedBudget()
                )

                .transportMode(
                        savedRequest.getTransportMode()
                )

                .hotelRequired(
                        savedRequest.getHotelRequired()
                )

                .hotelPreference(
                        savedRequest.getHotelPreference()
                )

                .advancePayment(
                        savedRequest.getAdvancePayment()
                )

                .additionalNotes(
                        savedRequest.getAdditionalNotes()
                )

                .status(
                        savedRequest.getStatus().name()
                )

                .build();
    }

    @Override
    public List<TravelRequestResponse>
    getRequestsByEmployee(Long employeeId) {

        List<TravelRequest> requests =
                travelRequestRepository
                        .findByEmployee_Id(employeeId);

        return requests.stream()

                .map(request ->
                        TravelRequestResponse.builder()

                                .id(request.getId())

                                .requestCode(
                                        request.getRequestCode()
                                )

                                .employeeName(
                                        request.getEmployee()
                                                .getFullName()
                                )

                                .managerName(
                                        request.getManager()
                                                .getFullName()
                                )

                                .fromLocation(
                                        request.getFromLocation()
                                )

                                .destination(
                                        request.getDestination()
                                )

                                .purpose(
                                        request.getPurpose()
                                )

                                .startDate(
                                        request.getStartDate()
                                )

                                .endDate(
                                        request.getEndDate()
                                )

                                .estimatedBudget(
                                        request.getEstimatedBudget()
                                )

                                .transportMode(
                                        request.getTransportMode()
                                )

                                .hotelRequired(
                                        request.getHotelRequired()
                                )

                                .hotelPreference(
                                        request.getHotelPreference()
                                )

                                .advancePayment(
                                        request.getAdvancePayment()
                                )

                                .additionalNotes(
                                        request.getAdditionalNotes()
                                )

                                .status(
                                        request.getStatus().name()
                                )

                                .build()
                )

                .toList();
    }

    @Override
    public TravelRequestResponse cancelRequest(
            Long requestId
    ) {

        TravelRequest request =
                travelRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Request not found"
                                )
                        );

        // ONLY DRAFT CAN CANCEL

        if(request.getStatus()
                != TravelRequestStatus.DRAFT){

            throw new RuntimeException(
                    "Only draft requests can be cancelled"
            );
        }

        request.setStatus(
                TravelRequestStatus.CANCELLED
        );

        TravelRequest updatedRequest =
                travelRequestRepository.save(request);

        return TravelRequestResponse.builder()

                .id(updatedRequest.getId())

                .requestCode(
                        updatedRequest.getRequestCode()
                )

                .employeeName(
                        updatedRequest
                                .getEmployee()
                                .getFullName()
                )

                .managerName(
                        updatedRequest
                                .getManager()
                                .getFullName()
                )

                .fromLocation(
                        updatedRequest.getFromLocation()
                )

                .destination(
                        updatedRequest.getDestination()
                )

                .purpose(
                        updatedRequest.getPurpose()
                )

                .startDate(
                        updatedRequest.getStartDate()
                )

                .endDate(
                        updatedRequest.getEndDate()
                )

                .estimatedBudget(
                        updatedRequest.getEstimatedBudget()
                )

                .transportMode(
                        updatedRequest.getTransportMode()
                )

                .hotelRequired(
                        updatedRequest.getHotelRequired()
                )

                .hotelPreference(
                        updatedRequest.getHotelPreference()
                )

                .advancePayment(
                        updatedRequest.getAdvancePayment()
                )

                .additionalNotes(
                        updatedRequest.getAdditionalNotes()
                )

                .status(
                        updatedRequest
                                .getStatus()
                                .name()
                )

                .build();
    }


    @Override
    public List<TravelRequestResponse>
    getRequestsByManager(Long managerId) {

        List<TravelRequest> requests =

                travelRequestRepository
                        // Enforces department-based visibility because manager_id is assigned
                        // at creation time based on the employee's department.
                        .findByManagerId(managerId);


        return requests.stream()

                .map(request ->

                        TravelRequestResponse.builder()

                                .id(request.getId())

                                .requestCode(
                                        request.getRequestCode()
                                )

                                .employeeName(
                                        request.getEmployee() != null
                                                ? request.getEmployee().getFullName()
                                                : request.getManager().getFullName()
                                )
//                                .employeeName(
//                                        request.getEmployee()
//                                                .getFullName()
//                                )

                                .managerName(
                                        request.getManager()
                                                .getFullName()
                                )

                                .fromLocation(
                                        request.getFromLocation()
                                )

                                .destination(
                                        request.getDestination()
                                )

                                .purpose(
                                        request.getPurpose()
                                )

                                .startDate(
                                        request.getStartDate()
                                )

                                .endDate(
                                        request.getEndDate()
                                )

                                .estimatedBudget(
                                        request.getEstimatedBudget()
                                )

                                .transportMode(
                                        request.getTransportMode()
                                )

                                .hotelRequired(
                                        request.getHotelRequired()
                                )

                                .hotelPreference(
                                        request.getHotelPreference()
                                )

                                .advancePayment(
                                        request.getAdvancePayment()
                                )

                                .additionalNotes(
                                        request.getAdditionalNotes()
                                )

                                .status(
                                        request.getStatus().name()
                                )

                                .build()
                )

                .toList();
    }

    @Override
    public TravelRequestResponse approveRequest(
            Long requestId
    ) {
        // Force callers to use the ownership-validating overload.
        throw new RuntimeException("managerId required");
    }

    @Override
    public TravelRequestResponse approveRequest(
            Long requestId,
            Long managerId
    ) {

        TravelRequest request =

                travelRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                "Request not found"
                                )
                        );

        if (request.getManager() == null
                || request.getManager().getId() == null
                || !request.getManager().getId().equals(managerId)) {
            throw new RuntimeException("Not allowed");
        }

        if(request.getStatus()
                != TravelRequestStatus.DRAFT){

            throw new RuntimeException(
                    "Only draft request can be approved"
            );
        }

        request.setStatus(
                TravelRequestStatus.MANAGER_APPROVED
        );

        TravelRequest updatedRequest =
                travelRequestRepository.save(request);

        return mapToResponse(updatedRequest);
    }

    @Override
    public TravelRequestResponse rejectRequest(
            Long requestId
    ) {
        // Force callers to use the ownership-validating overload.
        throw new RuntimeException("managerId required");
    }

    @Override
    public TravelRequestResponse rejectRequest(
            Long requestId,
            Long managerId
    ) {

        TravelRequest request =

                travelRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                "Request not found"
                                )
                        );

        if (request.getManager() == null
                || request.getManager().getId() == null
                || !request.getManager().getId().equals(managerId)) {
            throw new RuntimeException("Not allowed");
        }

        if(request.getStatus()
                != TravelRequestStatus.DRAFT){

            throw new RuntimeException(
                    "Only draft request can be rejected"
            );
        }

        request.setStatus(
                TravelRequestStatus.REJECTED
        );

        TravelRequest updatedRequest =
                travelRequestRepository.save(request);

        return mapToResponse(updatedRequest);
    }


    @Override
    public List<TravelRequestResponse>
    getFinanceRequests() {

        List<TravelRequest> requests =
                travelRequestRepository.findAll();

        return requests.stream()

                .filter(request ->

                        request.getStatus()
                                == TravelRequestStatus.MANAGER_APPROVED
                )

                .map(this::mapToResponse)

                .toList();
    }

    @Override
    public TravelRequestResponse financeApproveRequest(
            Long requestId, String remarks) {

        TravelRequest request =
                travelRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Request not found"
                                )
                        );

        if(request.getStatus()
                != TravelRequestStatus.MANAGER_APPROVED){

            throw new RuntimeException(
                    "Only manager approved requests allowed"
            );
        }

        request.setStatus(
                TravelRequestStatus.FINANCE_APPROVED
        );

        // =====================================
        // FINANCE AUDIT INFO
        // =====================================

        if (remarks == null || remarks.trim().isEmpty()) {
            request.setFinanceRemarks("Approved by finance team");
        } else {
            request.setFinanceRemarks(remarks);
        }

        request.setFinanceActionBy(
                "Finance Admin"
        );

        request.setFinanceActionDate(
                LocalDateTime.now()
        );

        TravelRequest updated =
                travelRequestRepository.save(request);

        return mapToResponse(updated);
    }


    @Override
    public TravelRequestResponse financeRejectRequest(
            Long requestId, String remarks
    ) {

        TravelRequest request =
                travelRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Request not found"
                                )
                        );

        if(request.getStatus()
                != TravelRequestStatus.MANAGER_APPROVED){

            throw new RuntimeException(
                    "Only manager approved requests allowed"
            );
        }

        request.setStatus(
                TravelRequestStatus.REJECTED
        );

        // =====================================
        // FINANCE AUDIT INFO
        // =====================================

        if (remarks == null || remarks.trim().isEmpty()) {
            request.setFinanceRemarks("Rejected by finance team");
        } else {
            request.setFinanceRemarks(remarks);
        }

        request.setFinanceActionBy(
                "Finance Admin"
        );

        request.setFinanceActionDate(
                LocalDateTime.now()
        );

        request.setFinanceRemarks(
                remarks
        );

        TravelRequest updated =
                travelRequestRepository.save(request);

        return mapToResponse(updated);
    }

    @Override
    public List<TravelRequestResponse>
    getFinanceDecisionHistory() {

        return travelRequestRepository
                .findAll()
                .stream()

                .filter(request ->

                        request.getStatus().name()
                                .equals("FINANCE_APPROVED")

                                ||

                                request.getStatus().name()
                                        .equals("REJECTED")

                                ||

                                request.getStatus().name()
                                        .equals("BOOKED")

                                ||

                                request.getStatus().name()
                                        .equals("COMPLETED")
                )

                .map(this::mapToResponse)

                .toList();
    }

    private TravelRequestResponse
    mapToResponse(TravelRequest request) {

        String roleTitle = null;
        String employeeName = null;
        String department = null;

        // =====================================
        // FLOW 1
        // Employee -> Manager -> Finance
        // =====================================

        if (request.getEmployee() != null) {

            employeeName =
                    request.getEmployee()
                            .getFullName();

            department =
                    request.getEmployee()
                            .getDepartment();

            roleTitle =
                    request.getEmployee()
                            .getRole()
                            .getName()
                            .name();
        }

        // =====================================
        // FLOW 2
        // Manager -> Finance
        // =====================================

        else if (request.getManager() != null) {

            employeeName =
                    request.getManager()
                            .getFullName();

            department =
                    request.getManager()
                            .getDepartment();

            roleTitle =
                    request.getManager()
                            .getRole()
                            .getName()
                            .name();
        }

        return TravelRequestResponse.builder()


                .id(request.getId())

                .requestCode(
                        request.getRequestCode()
                )

                .employeeName(employeeName)

                .roleTitle(roleTitle)

                .department(department)

                .managerName(
                        request.getManager() != null
                                ? request.getManager().getFullName()
                                : null
                )

                .destination(
                        request.getDestination()
                )

                .startDate(
                        request.getStartDate()
                )

                .endDate(
                        request.getEndDate()
                )

                .estimatedBudget(
                        request.getEstimatedBudget()
                )

                .status(
                        request.getStatus().name()
                )
                .financeRemarks(
                        request.getFinanceRemarks()
                )

                .financeActionBy(
                        request.getFinanceActionBy()
                )

                .financeActionDate(
                        request.getFinanceActionDate()
                )

                .build();
    }

    @Override
    public TravelRequestResponse createManagerRequest(
            CreateManagerTravelRequestDto dto
    ) {


        User manager = userRepository
                .findById(dto.getManagerId())
                .orElseThrow(() ->
                        new RuntimeException("Manager Not Found")
                );

        TravelRequest request =
                new TravelRequest();

        request.setManager(manager);

        request.setDestination(
                dto.getDestination()
        );

        request.setPurpose(
                dto.getPurpose()
        );

        request.setStartDate(
                dto.getStartDate()
        );

        request.setEndDate(
                dto.getEndDate()
        );

        request.setTransportMode(
                dto.getTransportMode()
        );

        request.setHotelRequired(
                dto.getHotelRequired()
        );

        request.setHotelPreference(
                dto.getHotelPreference()
        );

        request.setEstimatedBudget(
                dto.getEstimatedBudget()
        );

        request.setAdvancePayment(
                dto.getAdvancePayment()
        );

        request.setAdditionalNotes(
                dto.getAdditionalNotes()
        );

        request.setEmployee(null);


        // =====================================
        // IMPORTANT
        // =====================================

        request.setStatus(TravelRequestStatus.MANAGER_APPROVED);

        request.setRequestCode(
                "MGR-" + System.currentTimeMillis()
        );

        TravelRequest savedRequest =
                travelRequestRepository
                        .save(request);

        return mapToResponse(
                savedRequest
        );
    }

    @Override
    public List<TravelRequestResponse>
    getManagerRequestHistory(Long managerId) {

        List<TravelRequest> requests =

                travelRequestRepository
                        .findByManager_IdAndEmployeeIsNull(
                                managerId
                        );

        return requests.stream()

                .map(this::mapToResponse)

                .toList();
    }

}
