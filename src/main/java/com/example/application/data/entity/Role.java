package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role extends AbstractEntity implements GrantedAuthority {

    private String name;

    public Role() {

    }

    public Role(Integer id, String name) {
        this.setId(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return getName();
    }
}
