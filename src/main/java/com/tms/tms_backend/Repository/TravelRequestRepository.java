package com.tms.tms_backend.Repository;

import com.tms.tms_backend.Entity.TravelRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRequestRepository
        extends JpaRepository<TravelRequest, Long> {
    List<TravelRequest> findByEmployee_Id(Long employeeId);

    List<TravelRequest> findByManager_IdAndEmployeeIsNull(
            Long managerId
    );
    
    // Manager dashboard: fetch only requests assigned to the logged-in manager
    List<TravelRequest>
    findByManagerId(
            Long managerId
    );
}
