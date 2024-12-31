package backend.properties_crud.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name="users",
    uniqueConstraints=@UniqueConstraint(columnNames={"email","phone"})
)
public class User{

    @JsonIgnore
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    private String phone;

    private String email; 
    // will be considered as username in spring security

    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "user", 
    cascade = CascadeType.ALL, 
    orphanRemoval = true,fetch=FetchType.LAZY)
    private List<Property> properties;
}