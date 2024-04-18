package com.qavi.attendanceapplication.usermanagement.controllers.permission;

import com.qavi.attendanceapplication.usermanagement.entities.permisions.Permission;
import com.qavi.attendanceapplication.usermanagement.services.permission.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    //GetAll Permission controller
    @ResponseBody
    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermission() {
        List<Permission> permissions = permissionService.getAllPermission();
        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

    //Get Permission By id
    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        Permission permission = permissionService.getPermissionById(id);
        return new ResponseEntity<>(permission, HttpStatus.OK);
    }

    //Create a Permission
    @ResponseBody
    @PostMapping("/add-permission")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        Permission createdPermission = permissionService.createPermission(permission);
        return new ResponseEntity<>(createdPermission, HttpStatus.CREATED);
    }

    //update Permission
    @ResponseBody
    @PutMapping("/{id}")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission, @PathVariable Long id) {
        Permission updatedPermission = permissionService.updatePermission(permission, id);
        return new ResponseEntity<>(updatedPermission, HttpStatus.OK);
    }

    //Delete Permission
    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
