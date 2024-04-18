package com.qavi.attendanceapplication.usermanagement.services.permission;

import com.qavi.attendanceapplication.usermanagement.entities.permisions.Permission;
import com.qavi.attendanceapplication.usermanagement.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    // Get All permissions
    public List<Permission> getAllPermission() {
        return permissionRepository.findAll();
    }

    //Get Permission By ID
    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id).get();
    }

    //Create Permission
    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    //Update Permission
    public Permission updatePermission(Permission permission, Long id) {
        Permission existingPermission = permissionRepository.findById(id).get();
        if (existingPermission != null) {
            existingPermission.setName(permission.getName());
            return permissionRepository.save(existingPermission);
        } else {
            return null;
        }
    }

    //Delete Permission
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id).get();
        if (permission != null) {
            permissionRepository.delete(permission);
        }
    }
}
