package com.qavi.attendanceapplication.usermanagement.entities.role;


import com.qavi.attendanceapplication.usermanagement.entities.permisions.PermissionAssigned;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    Set<PermissionAssigned> permissionAssigned;

    public Role(String name, Set<PermissionAssigned> permissions) {
        this.name = name;
        this.permissionAssigned = permissions;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {

        final Role other = (Role) obj;
        if (this.id.equals(other.id)) {
            return true;
        } else {
            return false;
        }
    }

    public Role(String roleName) {
        this.name = roleName;
    }

}
