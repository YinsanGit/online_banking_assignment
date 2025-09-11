package com.finalBanking.demo.Repository;


import com.finalBanking.demo.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface permissionRepository extends JpaRepository<Permission,Long> {
    Optional<Permission> findByName(String name);
}
