package backend.properties_crud.repository;

import backend.properties_crud.entity.Property;
import backend.properties_crud.entity.PropertyType;
import backend.properties_crud.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
  List<Property> findByUser(User user);

  Property findByIdAndUser(Long id, User user);

  List<Property> findByAddressContaining(String address);

  List<Property> findByType(PropertyType type);

  List<Property> findByTypeAndAddressContaining(PropertyType type, String address);

  List<Property> findByAddressContainingAndRentBetween(
      String address, Integer minRent, Integer maxRent);

  List<Property> findByTypeAndRentBetween(PropertyType type, Integer minRent, Integer maxRent);

  List<Property> findByTypeAndAddressContainingAndRentBetween(
      PropertyType type, String address, Integer minRent, Integer maxRent);
}
