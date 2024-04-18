package com.qavi.attendanceapplication.organizationmanagement.repositories;

import com.qavi.attendanceapplication.organizationmanagement.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {


    @Query("SELECT o FROM Organization o LEFT JOIN FETCH o.members WHERE o.id = :orgId")
    Optional<Organization> findByIdWithMembers(@Param("orgId") Long orgId);

    @Query(value = "SELECT organization_id FROM organization_users WHERE users_id = :id", nativeQuery = true)
    Long findByOrganizationId(Long id);

    @Query("SELECT o.id FROM Organization o JOIN o.members m WHERE m.id = :memberId")
    List<Long> findOrgIdsByMemberId(@Param("memberId") Long memberId);


    Optional<Organization> findByEmail(String email);

    Optional<Organization> findByName(String email);
}
