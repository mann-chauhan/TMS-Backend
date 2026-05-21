package com.tms.tms_backend.Repository;

import com.tms.tms_backend.Entity.Role;
import com.tms.tms_backend.Entity.Role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}