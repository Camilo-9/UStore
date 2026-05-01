
package edu.unimagdalena.uStore.security.domine;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class AppUser implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id",
                     referencedColumnName = "id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked = true;

    public AppUser(){
    }

    public AppUser(UUID id, String username, String password, Set<Role> roles, boolean enabled,
                   boolean accountNonLocked){
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles != null ? roles:new HashSet<>();
        this.enabled = enabled;
        this.accountNonLocked = accountNonLocked;
    }

    public UUID getId(){
        return id;
    }

    public void setId(UUID id){
        this.id = id;
    }

    @Override
    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    @Override
    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public Set<Role> getRoles(){
        return roles;
    }

    public void setRoles(Set<Role> roles){
        this.roles = roles != null ? roles:new HashSet<>();
    }

    @Override
    public boolean isEnabled(){
        return enabled;
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    @Override
    public boolean isAccountNonLocked(){
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked){
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.name())).toList();
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public String toString(){
        return "AppUser{"+
                "id="+ id+
                ", username='"+ username + '\''+
                ", roles="+ roles+
                ", enabled="+ enabled+
                ", accountNonLocked="+ accountNonLocked+
                '}';
    }
}
