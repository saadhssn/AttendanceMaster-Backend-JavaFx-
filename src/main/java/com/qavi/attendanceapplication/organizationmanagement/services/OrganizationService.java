package com.qavi.attendanceapplication.organizationmanagement.services;

import com.qavi.attendanceapplication.organizationmanagement.entities.Organization;
import com.qavi.attendanceapplication.organizationmanagement.models.OrganizationModel;
import com.qavi.attendanceapplication.organizationmanagement.repositories.OrganizationRepository;
import com.qavi.attendanceapplication.usermanagement.entities.user.User;
import com.qavi.attendanceapplication.usermanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {
    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    UserRepository userRepository;


    public Boolean deleteOrganization(Long id) {
        try {
            organizationRepository.deleteById(id);

        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    public String createOrganization(Organization organization, Long adminId) {
        Optional<User> userOptional = userRepository.findById(adminId);

        if (userOptional.isPresent()) {
            User admin = userOptional.get();

            if ("[ROLE_ADMIN]".equals(admin.getRole().toString())) {
                Optional<Organization> organizationByEmail = organizationRepository.findByEmail(organization.getEmail());
                Optional<Organization> organizationByName = organizationRepository.findByName(organization.getName());

                if (organizationByEmail.isEmpty() && organizationByName.isEmpty()) {
                    organization.setUsers(Collections.singletonList(admin));
                    Organization saved = organizationRepository.save(organization);
                    String organizationId = String.valueOf(saved.getId());
                    return organizationId;
                } else {
                    return organizationByEmail.isPresent() ? "Email already exists" : "Name already exists";
                }

            } else {
                return "User must have ADMIN role to register an Organization";
            }
        } else {
            return "User does not exist";
        }
    }



    public Organization getOrganization(Long id) {
        return organizationRepository.findById(id).get();
    }

    public boolean updateOrganization(OrganizationModel organizationModel, Long id) {
        try {
            Organization organization = organizationRepository.findById(id).get();
            organization.setName(organizationModel.getName());
            organization.setAddress(organizationModel.getAddress());
            organization.setPhoneNumber(organizationModel.getPhoneNumber());
            organization.setDescription(organizationModel.getDescription());
            organization.setEmail(organizationModel.getEmail());
            organizationRepository.save(organization);
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Long getOrganizationIds(Long id) {
        return organizationRepository.findByOrganizationId(id);
    }
}
