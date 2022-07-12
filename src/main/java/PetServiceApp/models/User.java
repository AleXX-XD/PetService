package PetServiceApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.bytecode.internal.javassist.BulkAccessor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name="users")
public class User implements UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    @Column(name = "account_non_locked")
    private boolean accountNonLocked;
    @Column(name = "failed_attempt")
    private int failedAttempt;
    @Column(name = "lock_time")
    private Date lockTime;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Pet> petList = new ArrayList<>();

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void resetFailedAttempt() {
        accountNonLocked = true;
        failedAttempt = 0;
        lockTime = null;
    }

    public void lockedAccount() {
        accountNonLocked = false;
        lockTime = new Date();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public static Builder builder() {
        return new User().new Builder();
    }

    public class Builder {

        private Builder() {
        }

        public Builder setUsername(String username) {
            User.this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            User.this.password = password;
            return this;
        }

        public Builder setAccountNonLocked(boolean accountNonLocked) {
            User.this.accountNonLocked = accountNonLocked;
            return this;
        }

        public Builder setRoles(Set<Role> roles) {
            User.this.roles = roles;
            return this;
        }

        public Builder setFailedAttempt(int failedAttempt) {
            User.this.failedAttempt = failedAttempt;
            return this;
        }

        public User build() {
            return User.this;
        }

    }
}
