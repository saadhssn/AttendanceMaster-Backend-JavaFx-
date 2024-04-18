package com.qavi.attendanceapplication.feesmanagement.repositories;

import com.qavi.attendanceapplication.feesmanagement.entities.Fees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FeesRepository extends JpaRepository<Fees, Long> {



    List<Fees> findAllByOrganizationId(Long orgId);

    @Query("SELECT f.forMonthOf AS forMonth, COUNT(f.member.id) AS paid FROM Fees f WHERE f.organization.id = :orgId AND f.forYearOf = :year GROUP BY f.forMonthOf")
    List<Map<String, Long>> getPaidUsersByMonthAndYear(@Param("orgId") Long orgId, @Param("year") String year);


}
