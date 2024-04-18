package com.qavi.attendanceapplication.usermanagement.repositories;

import com.qavi.attendanceapplication.usermanagement.entities.permisions.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByName(String name);
}
