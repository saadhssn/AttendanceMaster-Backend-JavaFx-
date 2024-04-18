package com.qavi.attendanceapplication.feesmanagement.utils;

import com.qavi.attendanceapplication.feesmanagement.entities.Fees;
import com.qavi.attendanceapplication.feesmanagement.models.FeesModel;

public class FeesConveter {
    public static FeesModel ConvertFeesToFeesModel(Fees fees) {
        FeesModel feesModel = new FeesModel();
        feesModel.setAmount(fees.getAmount());
        feesModel.setCreatedAt(fees.getCreatedAt());
        feesModel.setId(fees.getId());
        feesModel.setForYearOf(fees.getForYearOf());
        feesModel.setForMonthOf(fees.getForMonthOf());
        feesModel.setMemberId(fees.getMember().getId());
        feesModel.setOrganizationId(fees.getOrganization().getId());
        return feesModel;
    }

}
