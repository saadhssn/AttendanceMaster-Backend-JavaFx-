package com.qavi.attendanceapplication.membermanagement.repositories;

import com.qavi.attendanceapplication.membermanagement.entities.MemberImages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberImageRepository extends JpaRepository<MemberImages, Long> {
}
