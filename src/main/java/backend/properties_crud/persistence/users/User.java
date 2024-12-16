package backend.properties_crud.persistence.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String email;
    private String password;
    // FetchType EAGER tells to fetch Roles at same time of retrieval of User
    // Roles is child of User
    // CascaseType.ALL tells to perform all operations effects of User to Roles

    public User(String firstName,String lastName,String phone,String email,
    String password){
        this.firstName=firstName;
        this.lastName=lastName;
        this.phone=phone;
        this.email=email;
        this.password=password;
    }
}