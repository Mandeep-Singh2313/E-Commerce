package com.project.ecommerce.repo;

import com.project.ecommerce.entity.AppRole;
import com.project.ecommerce.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
     Optional<Role> findByRoleName(AppRole roleName);
}
