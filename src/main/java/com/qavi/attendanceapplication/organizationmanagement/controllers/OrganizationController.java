package com.qavi.attendanceapplication.organizationmanagement.controllers;

import com.qavi.attendanceapplication.organizationmanagement.entities.Organization;
import com.qavi.attendanceapplication.organizationmanagement.models.OrganizationModel;
import com.qavi.attendanceapplication.organizationmanagement.services.OrganizationService;
import com.qavi.attendanceapplication.organizationmanagement.utils.OrganizationConverter;
import com.qavi.attendanceapplication.usermanagement.models.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/organization")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    // Create
    @PostMapping("/create/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseModel> createOrganization(@RequestBody Organization organization, @PathVariable Long id) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Organization added successfully")
                .data(new Object())
                .build();
        responseModel.setMessage(organizationService.createOrganization(organization, id));
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    // Get Organization
    @GetMapping("/{id}")
    public ResponseEntity<OrganizationModel> getOrganization(@PathVariable Long id) {
        try {
            Organization organization = organizationService.getOrganization(id);
            OrganizationModel organizationModel = OrganizationConverter.convertOrganizationToOrganizationModel(organization);
            return new ResponseEntity<>(organizationModel, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // get All Organization
    @GetMapping("/getAll")
    public ResponseEntity<List<OrganizationModel>> getAllOrganization() {
        List<Organization> organizations = organizationService.getAllOrganizations();
        List<OrganizationModel> convertedList = new ArrayList<>();
        for (Organization organization : organizations) {
            convertedList.add(OrganizationConverter.convertOrganizationToOrganizationModel(organization));
        }
        return new ResponseEntity<List<OrganizationModel>>(convertedList, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Boolean> deleteOrganization(@PathVariable Long id) {
        Boolean deletedOrganization = organizationService.deleteOrganization(id);
        return new ResponseEntity<>(deletedOrganization, HttpStatus.OK);
    }

    //Update
    @PutMapping("update-organization/{id}")
    public ResponseEntity<ResponseModel> updateOrganization(@RequestBody OrganizationModel organizationModel, @PathVariable Long id) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Updated")
                .data(new Object())
                .build();
        if (!organizationService.updateOrganization(organizationModel, id)) {
            responseModel.setMessage("Updated Failed");
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    @GetMapping("getOrgId/{id}")
    public ResponseEntity<ResponseModel> getOrganizationId(@PathVariable Long id) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Fetched")
                .data(new Object())
                .build();
        Long user = organizationService.getOrganizationIds(id);
        if (user == null) {
            responseModel.setMessage("Fetch Failed");
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
        } else {
            responseModel.setData(user);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

}
