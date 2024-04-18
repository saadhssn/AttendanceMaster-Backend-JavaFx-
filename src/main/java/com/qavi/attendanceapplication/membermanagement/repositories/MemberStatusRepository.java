package com.qavi.attendanceapplication.membermanagement.repositories;


import com.qavi.attendanceapplication.membermanagement.entities.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberStatusRepository extends JpaRepository<MemberStatus, Long> {
    Optional<MemberStatus> findByYearAndMonthAndOrgId(Long year, String month,Long orgId);
}
