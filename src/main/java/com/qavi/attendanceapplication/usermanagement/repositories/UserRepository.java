package com.qavi.attendanceapplication.usermanagement.repositories;

import com.qavi.attendanceapplication.membermanagement.entities.Member;
import com.qavi.attendanceapplication.usermanagement.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT m.* FROM Users m JOIN organization_users om ON m.id = om.users_id WHERE om.organization_id = :orgId",nativeQuery = true)
    List<User> findAllUserByOrganizationId(@Param("orgId") Long orgId);

    @Query(value = "SELECT m.* FROM Users m JOIN organization_users om ON m.id = om.users_id WHERE om.organization_id = :orgId ORDER BY m.registered_at DESC LIMIT 2", nativeQuery = true)
    List<User> findRecentUsersByOrganizationId(@Param("orgId") Long orgId);


    @Query(value = "Select * from users where email=?1", nativeQuery = true)
    User getUser(String email);


    @Query(value = "select * from users where email=?1 and auth_type=?2", nativeQuery = true)
    Optional<User> findByEmail(String email, String authType);

    Optional<User> findById(Long id);

    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "update users set password = :newPassword where email=:email", nativeQuery = true)
    void updatePassword(String email, String newPassword);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET enabled = NOT enabled WHERE id = :id", nativeQuery = true)
    void toggleUserStatus(@Param("id") Long id);

}
