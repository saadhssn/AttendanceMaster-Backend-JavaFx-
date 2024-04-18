package com.qavi.attendanceapplication.usermanagement.services.role;

import com.qavi.attendanceapplication.usermanagement.entities.role.Role;
import com.qavi.attendanceapplication.usermanagement.models.PermissionBitsModel;
import com.qavi.attendanceapplication.usermanagement.models.RoleModel;
import com.qavi.attendanceapplication.usermanagement.repositories.PermissionRepository;
import com.qavi.attendanceapplication.usermanagement.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PermissionRepository permissionRepository;

    public boolean addRole(Role role) {
        try {
            roleRepository.save(role);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean createRoles() {
        try {
            // Create roles with specific names
            String[] rolesToCreate = {"ROLE_ADMIN","ROLE_EMPLOYEE"};
            List<Role> roles = roleRepository.findAll();
            for (String role : rolesToCreate) {
                if (roles.stream().filter(roleIterate -> roleIterate.getName().equalsIgnoreCase(role)).collect(Collectors.toList()).isEmpty()) {
                    roleRepository.save(new Role(role));
                }
            }
            // Save roles to the database
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean assignPermission(PermissionBitsModel permissionBitsModel, Long roleId) {
        try {
            roleRepository.assignPermission(permissionBitsModel.getPermissionBit(), permissionRepository.findByName(permissionBitsModel.getPermission()).getId(), roleRepository.findById(roleId).get().getId());
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean deleteRoles(Long roleId) {
        try {
            roleRepository.deleteById(roleId);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean update_role(Long roleId, RoleModel roleModel) {
        Role foundRole = roleRepository.findById(roleId).get();
        if (foundRole != null) {
            try {
                roleModel.setName(foundRole.getName());
                return true;
            } catch (Exception e) {
                System.out.println(e);
                return false;
            }
        } else {
            System.out.println("Role Not found");
            return false;
        }
    }
}
