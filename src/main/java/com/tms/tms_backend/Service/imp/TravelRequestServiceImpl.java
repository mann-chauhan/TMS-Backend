package com.tms.tms_backend.Service.imp;

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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelRequestServiceImpl
        implements TravelRequestService {

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

        // MANAGER

        User manager = userRepository
                .findByDepartmentAndRole_Name(
                        employee.getDepartment(),
                        Role.RoleName.MANAGER
                )
                .orElseThrow(() ->
                        new RuntimeException("Manager not found")
                );

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
                        .findByManager_Id(managerId);

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
    public TravelRequestResponse approveRequest(
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

        return TravelRequestResponse.builder()

                .id(updatedRequest.getId())

                .requestCode(
                        updatedRequest.getRequestCode()
                )

                .employeeName(
                        updatedRequest.getEmployee()
                                .getFullName()
                )

                .managerName(
                        updatedRequest.getManager()
                                .getFullName()
                )

                .destination(
                        updatedRequest.getDestination()
                )

                .status(
                        updatedRequest.getStatus().name()
                )

                .build();
    }

    @Override
    public TravelRequestResponse rejectRequest(
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

        return TravelRequestResponse.builder()

                .id(updatedRequest.getId())

                .requestCode(
                        updatedRequest.getRequestCode()
                )

                .employeeName(
                        updatedRequest.getEmployee()
                                .getFullName()
                )

                .managerName(
                        updatedRequest.getManager()
                                .getFullName()
                )

                .destination(
                        updatedRequest.getDestination()
                )

                .status(
                        updatedRequest.getStatus().name()
                )

                .build();
    }

}