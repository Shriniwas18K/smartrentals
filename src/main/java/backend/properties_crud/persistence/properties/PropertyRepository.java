package backend.properties_crud.persistence.properties;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

//implementation is given by spring data jpa implicitely
@Repository
public interface PropertyRepository extends JpaRepository<Property,Long>{

    //implicitely implementation provided by spring data jpa    
    List<Property> findByAddress(String address);

    //we can give explicit logic for custom queries
    @Query("SELECT p FROM Property p WHERE p.address LIKE %:address% AND p.rent > :minRent")
    List<Property> findPropertiesByAddressAndRent(@Param("address") String address, @Param("minRent") Integer minRent);

}

/*
In Spring Data JPA, you can define custom queries using the @Query annotation. Here's an example:
Custom Query Example
Java
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p FROM Property p WHERE p.address LIKE %:address% AND p.rent > :minRent")
    List<Property> findPropertiesByAddressAndRent(@Param("address") String address, @Param("minRent") Integer minRent);
} */

/* Some methods like save,findAll are implicitely available without writing in interface */

/*
For one field address we can have these methods possible to define by simply writing these method signatures
Spring data JPA gives implicitely implementations of below fields by itself.
Here are some examples of query methods using Spring Data JPA conventions:

Find/Get/Read Methods
Find by property: findByAddress(String address)
Find by property (ignore case): findByAddressIgnoreCase(String address)
Find by property (containing): findByAddressContaining(String address)
Find by property (starting with): findByAddressStartingWith(String address)
Find by property (ending with): findByAddressEndingWith(String address)

Find All Methods
Find all: findAll()
Find all by property: findAllByAddress(String address)

Count Methods
Count by property: countByAddress(String address)

Delete Methods
Delete by property: deleteByAddress(String address)

Exists Methods
Exists by property: existsByAddress(String address)

And/Or/Not Methods
Find by property and property: findByAddressAndOwnerName(String address, String ownerName)
Find by property or property: findByAddressOrOwnerName(String address, String ownerName)
Find by property not: findByAddressNot(String address)

In/NotIn Methods
Find by property in: findByAddressIn(List<String> addresses)
Find by property not in: findByAddressNotIn(List<String> addresses)

IsNull/IsNotNull Methods
Find by property is null: findByAddressIsNull()
Find by property is not null: findByAddressIsNotNull()

True/False Methods
Find by property is true: findByActiveIsTrue()
Find by property is false: findByActiveIsFalse()

Between Methods
Find by property between: findByRentBetween(Integer minRent, Integer maxRent)

GreaterThan/LessThan Methods
Find by property greater than: findByRentGreaterThan(Integer minRent)
Find by property less than: findByRentLessThan(Integer maxRent)

GreaterThanEqual/LessThanEqual Methods
Find by property greater than or equal: findByRentGreaterThanEqual(Integer minRent)
Find by property less than or equal: findByRentLessThanEqual(Integer maxRent)

Note that these are just examples and you can combine multiple keywords to create more complex query methods.
Many more methods for saving updating are available
*/




/* These things can be done for high customization with spring data jpa 

Here are some more examples of what you can do with Spring Data JPA:
1. Paginating Results
You can use the Pageable interface to paginate results:
Java
public interface PropertyRepository extends JpaRepository<Property, Long> {

    Page<Property> findByAddressContaining(String address, Pageable pageable);
}
2. Sorting Results
You can use the Sort interface to sort results:
Java
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByAddressContaining(String address, Sort sort);
}
3. Using SpEL Expressions
You can use Spring Expression Language (SpEL) expressions to define dynamic queries:
Java
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p FROM Property p WHERE p.address LIKE %:#{#address}%")
    List<Property> findPropertiesByAddress(@Param("address") String address);
}
4. Using Querydsl
Querydsl is a library that allows you to define typesafe queries. You can use it with Spring Data JPA:
Java
public interface PropertyRepository extends JpaRepository<Property, Long>, QuerydslPredicateExecutor<Property> {

    @Override
    default Predicate getPredicate(Property property) {
        QProperty qProperty = QProperty.property;
        return qProperty.address.contains(property.getAddress());
    }
}
5. Using Projections
Projections allow you to retrieve only a subset of fields from an entity:
Java
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p.address, p.rent FROM Property p WHERE p.address LIKE %:address%")
    List<PropertyProjection> findPropertiesByAddress(@Param("address") String address);
}

public interface PropertyProjection {
    String getAddress();
    Integer getRent();
}
6. Using Specifications
Specifications allow you to define dynamic queries using a fluent API:
Java
public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {

    default List<Property> findPropertiesByAddressAndRent(String address, Integer rent) {
        Specification<Property> specification = (root, query, cb) -> {
            Predicate addressPredicate = cb.like(root.get("address"), "%" + address + "%");
            Predicate rentPredicate = cb.gt(root.get("rent"), rent);
            return cb.and(addressPredicate, rentPredicate);
        };
        return findAll(specification);
    }
}
7. Using Custom Repository Implementation
You can define a custom repository implementation to add custom functionality:
Java
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findPropertiesByAddressAndRent(String address, Integer rent);
}

public class PropertyRepositoryImpl implements PropertyRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Property> findPropertiesByAddressAndRent(String address, Integer rent) {
        Query query = entityManager.createQuery("SELECT p FROM Property p WHERE p.address LIKE %:address% AND p.rent > :rent");
        query.setParameter("address", address);
        query.setParameter("rent", rent);
        return query.getResultList();
    }
}

public interface PropertyRepositoryCustom {
    List<Property> findPropertiesByAddressAndRent(String address, Integer rent);
}*/