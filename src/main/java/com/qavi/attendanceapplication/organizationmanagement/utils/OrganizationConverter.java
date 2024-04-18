package com.qavi.attendanceapplication.organizationmanagement.utils;

import com.qavi.attendanceapplication.organizationmanagement.entities.Organization;
import com.qavi.attendanceapplication.organizationmanagement.models.OrganizationModel;

public class OrganizationConverter {

    public static OrganizationModel convertOrganizationToOrganizationModel(Organization organization) {
        OrganizationModel organizationModel = new OrganizationModel();
        organizationModel.setId(organization.getId());
        organizationModel.setName(organization.getName());
        organizationModel.setEmail(organization.getEmail());
        organizationModel.setPhoneNumber(organization.getPhoneNumber());
        organizationModel.setDescription(organization.getDescription());
        organizationModel.setAddress(organization.getAddress());
        organizationModel.setActive(organization.getActive());
        organizationModel.setCreatedAt(organization.getCreatedAt());
        return organizationModel;
    }
}
