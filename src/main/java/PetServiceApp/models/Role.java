package PetServiceApp.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "User_role")
public class Role implements GrantedAuthority {
    @Id
    private int id;
    private String name;
    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role() {}

    @Override
    public String getAuthority() {
        return getName();
    }
}
