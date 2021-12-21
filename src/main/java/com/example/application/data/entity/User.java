package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import com.example.application.data.listener.UserListener;
import com.example.application.data.repository.LevelRepository;
import com.example.application.data.repository.UserSettingsRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@EntityListeners(UserListener.class)
@Entity
@Table(name = "_user", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class User extends AbstractEntity implements UserDetails {
    @Size(min = 2, max = 8)
    private String username;

    @Size(min = 4, max = 70)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private UserSettings userSettings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Rating> ratings;

    @Transient
    public String getStringRoles() {
        if (roles == null || roles.isEmpty()) {
            return "";
        }
        StringBuilder stringRoles = new StringBuilder();
        for (Role role : roles) {
            stringRoles.append(role.getName()).append(", ");
        }
        return stringRoles.substring(0, stringRoles.length() - 2);
    }

    public User() {
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(UserSettings userSettings) {
        this.userSettings = userSettings;
    }
}
