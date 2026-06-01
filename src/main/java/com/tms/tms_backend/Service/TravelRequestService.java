package com.tms.tms_backend.Service;

import com.tms.tms_backend.Dto.Request.CreateManagerTravelRequestDto;

import com.tms.tms_backend.Dto.Request.CreateTravelRequestDto;
import com.tms.tms_backend.Dto.Response.TravelRequestResponse;

import java.util.List;

public interface TravelRequestService {

    TravelRequestResponse cancelRequest(Long requestId);

    List<TravelRequestResponse> getRequestsByEmployee(Long employeeId);

    TravelRequestResponse createRequest(
            CreateTravelRequestDto request
    );


    //Manager Approval Module
    List<TravelRequestResponse>
    getRequestsByManager(Long managerId);

    TravelRequestResponse approveRequest(
            Long requestId
    );
    TravelRequestResponse rejectRequest(
            Long requestId
    );

    // Ownership validation (manager must own the request)
    TravelRequestResponse approveRequest(
            Long requestId,
            Long managerId
    );
    TravelRequestResponse rejectRequest(
            Long requestId,
            Long managerId
    );

    //Finance Approval Module
    List<TravelRequestResponse> getFinanceRequests();

    TravelRequestResponse financeApproveRequest(Long requestId, String remarks);

    TravelRequestResponse financeRejectRequest(Long requestId, String remarks);

    List<TravelRequestResponse> getFinanceDecisionHistory();


    //Manager New travel request

    TravelRequestResponse createManagerRequest(
            CreateManagerTravelRequestDto dto
    );

    List<TravelRequestResponse>
    getManagerRequestHistory(Long managerId);
}
