package com.qavi.attendanceapplication.feesmanagement.services;

import com.qavi.attendanceapplication.feesmanagement.entities.Fees;
import com.qavi.attendanceapplication.feesmanagement.entities.RequestFees;
import com.qavi.attendanceapplication.feesmanagement.models.FeesModel;
import com.qavi.attendanceapplication.feesmanagement.repositories.FeesRepository;
import com.qavi.attendanceapplication.feesmanagement.utils.FeesConveter;
import com.qavi.attendanceapplication.membermanagement.entities.Member;
import com.qavi.attendanceapplication.membermanagement.repositories.MemberRepository;
import com.qavi.attendanceapplication.organizationmanagement.entities.Organization;
import com.qavi.attendanceapplication.organizationmanagement.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FeesServices {

    @Autowired
    FeesRepository feesRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    MemberRepository memberRepository;

    public Boolean deleteFees(Long id) {
        try {
            feesRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    public FeesModel getFees(Long id) {
        Fees fees = feesRepository.findById(id).orElse(null);
        return FeesConveter.ConvertFeesToFeesModel(fees);
    }


    public List<Fees> getAllFees(Long OrgId) {
        return feesRepository.findAllByOrganizationId(OrgId);
    }

    public List<Map<String, Long>> getPaidUsersByMonthAndYear(Long orgId, String year) {
        return feesRepository.getPaidUsersByMonthAndYear(orgId, year);
    }


    public Long registerFees(FeesModel feesModel,Long OrgId) {
        try {
            Member member = memberRepository.findById(feesModel.getMemberId()).get();
            Organization organization = organizationRepository.findById(OrgId).get();
            Fees saved = new Fees();
            saved.setMember(member);
            saved.setOrganization(organization);
            saved.setAmount(feesModel.getAmount());
            saved.setCreatedAt(LocalDateTime.now());
            saved.setForMonthOf(feesModel.getForMonthOf());
            saved.setForYearOf(feesModel.getForYearOf());
            feesRepository.save(saved);
            return saved.getId();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean updateFees(RequestFees requestFees, Long id) {
        try {
            Fees fees = feesRepository.findById(id).get();
            fees.setAmount(requestFees.getAmount());
            fees.setForMonthOf(requestFees.getForMonthOf());
            fees.setForYearOf(requestFees.getForYearOf());
            feesRepository.save(fees);
        } catch (Exception e) {
            throw e;
        }
        return true;
    }
}
























