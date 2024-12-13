package backend.properties_crud.persistence.users;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.CascadeType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Collection;
import java.util.ArrayList;
import java.util.stream.Collectors;
import backend.properties_crud.persistence.roles.Role;

// its important to proide value of name parameter in case where field name contains more than one word 
// for execution of hibernate quires underhood, if remove the name value then we see runtime errors in console 
// though exceution is perfect and application source code compiles

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users",uniqueConstraints=@UniqueConstraint(columnNames={"email","phone"}))
public class User{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;
    private String phone;
    private String email;
    private String password;
    // FetchType EAGER tells to fetch Roles at same time of retrieval of User
    // Roles is child of User
    // CascaseType.ALL tells to perform all operations effects of User to Roles
    @ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinTable(
        name="user_roles",
        joinColumns=@JoinColumn(
            name="user_id",
            referencedColumnName="id"
        ),
        inverseJoinColumns=@JoinColumn(
            name="role_id",
            referencedColumnName="id"
        )
    )//this tells to maintain join table of role_id and user_id 
    private Collection<Role> roles;

    public User(String firstName,String lastName,String phone,String email,
    String password,List<String> roles){
        this.firstName=firstName;
        this.lastName=lastName;
        this.phone=phone;
        this.email=email;
        this.password=password;
        this.roles = roles.stream()
            .map(roleName -> new Role(roleName)) 
            .collect(Collectors.toCollection(ArrayList::new));
    }
}