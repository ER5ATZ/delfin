package org.delfin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Entity
@Table(name = "userdetails")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsEntity extends AbstractEntity implements UserDetails {
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private LocalDateTime expires = LocalDateTime.now().plusYears(50);
    @Column(nullable = false)
    private LocalDateTime pwexpires = LocalDateTime.now().plusYears(1);
    @Column(nullable = false)
    private int failedlogins;
    @Column(nullable = false)
    private boolean active = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "userrolemapping",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "roleid"))
    private Set<RoleMappingEntity> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerMappingEntity> customerMappings = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return LocalDateTime.now().isBefore(getExpires());
    }

    @Override
    public boolean isAccountNonLocked() {
        return getFailedlogins() < 4;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return LocalDateTime.now().isBefore(getExpires());
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    public void resetPassword(String newPassword) {
        setPassword(new BCryptPasswordEncoder().encode(newPassword));
        setPwexpires(LocalDateTime.now().plusYears(1));
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + getId() +
                ", username='" + getUsername() +
                ", email='" + getEmail() +
                ", password='" + getPassword() +
                ", expires=" + getExpires() +
                ", pwexpires=" + getPwexpires() +
                ", failedlogins=" + getFailedlogins() +
                ", active=" + isActive() +
                ", roles=" + roles +
                '}';
    }
}
