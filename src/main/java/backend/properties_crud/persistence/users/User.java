package backend.properties_crud.persistence.users;

import java.util.List;

import backend.properties_crud.persistence.properties.Property;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String email; // will be considered as username in spring security
    private String password;
  
    public User(String firstName,String lastName,String phone,String email,
    String password){
        this.firstName=firstName;
        this.lastName=lastName;
        this.phone=phone;
        this.email=email;
        this.password=password;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch=FetchType.LAZY)
    private List<Property> properties;
}