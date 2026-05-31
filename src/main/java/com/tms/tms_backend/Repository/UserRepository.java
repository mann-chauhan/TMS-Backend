package com.tms.tms_backend.Repository;

import com.tms.tms_backend.Entity.Role;
import com.tms.tms_backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByDepartmentAndRole_Name(
            String department,
            Role.RoleName roleName
    );

    // Used to resolve the single department manager during travel request creation
    Optional<User> findByDepartmentAndRole_Name(
            String department,
            Role.RoleName roleName
    );

    // Used to detect duplicates (to avoid IncorrectResultSizeDataAccessException).
    List<User> findAllByDepartmentAndRole_Name(
            String department,
            Role.RoleName roleName
    );
}
