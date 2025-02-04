package org.example.app.Models.Entities;

import jakarta.persistence.*;
import org.example.app.RoleType;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Role {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleType role;

    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();

    public Role() {

    }


    public Role(RoleType role) {
        this.role = role;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
