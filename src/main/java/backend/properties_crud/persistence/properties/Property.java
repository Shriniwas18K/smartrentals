package backend.properties_crud.persistence.properties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/* above imported annotations are useful for reducing boilerplate code and configuration */

/* tables are created by spring data jpa implicitely
when we pass this entity into JpaRepository interface
lombok provides all getter setter constructors here
*/

@Entity // tells to construct database schema or table using this when given to JpaRepository
@Data // this annotation will tell to provide getter setter for all fields of class
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; // given by database , 
    // hence not to be taken as input in constructor
    // hence we use builder , or alternatively we can use 
    // noargsconstructor with setter methods chained
    private String address;
    private String ownerName;
    private Float area;
    private Integer rent;
}