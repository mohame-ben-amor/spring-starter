package com.mareussite.backend.repository;

import java.util.Optional;

import com.mareussite.backend.entity.Enum.ERole;
import com.mareussite.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}