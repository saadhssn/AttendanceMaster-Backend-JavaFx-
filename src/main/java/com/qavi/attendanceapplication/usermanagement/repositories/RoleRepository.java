package com.qavi.attendanceapplication.usermanagement.repositories;


import com.qavi.attendanceapplication.usermanagement.entities.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "Select * from role where name=?1 limit 1", nativeQuery = true)
    Role searchByName(String name);


    @Query(value = "Select permission_id from permission_assigned where role_id=?1", nativeQuery = true)
    List<Long> searchPermissions(Long id);

    Collection<?> findByName(String name);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO permission_assigned(permission_bits,permission_id,role_id) values(?1,?2,?3)", nativeQuery = true)
    void assignPermission(String permission_bits, Long permission_id, Long role_id);

    @Transactional
    @Modifying
    @Query(value = "Delete from permission_assigned where role_id=?1 and privilege_id=?2", nativeQuery = true)
    void removePrivilege(Long role_id, Long privilege_id);

}

