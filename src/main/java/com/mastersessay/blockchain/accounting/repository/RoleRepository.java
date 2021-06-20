package com.mastersessay.blockchain.accounting.repository;

import com.mastersessay.blockchain.accounting.consts.UserRole;
import com.mastersessay.blockchain.accounting.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(UserRole name);
}
