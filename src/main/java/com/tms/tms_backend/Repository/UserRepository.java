package com.tms.tms_backend.Repository;

import com.tms.tms_backend.Entity.Role;
import com.tms.tms_backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByDepartmentAndRole_Name(
            String department,
            Role.RoleName roleName
    );

    Optional<User> findByDepartmentAndRole_Name(
            String department,
            Role.RoleName roleName
    );
}