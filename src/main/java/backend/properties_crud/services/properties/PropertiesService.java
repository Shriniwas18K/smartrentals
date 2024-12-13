package backend.properties_crud.services.properties;

import backend.properties_crud.persistence.properties.Property;
import java.util.Optional;
import java.util.List;
// Controllers make use only methods in the interfaces of service layer
// This allows the service layer to be independently developed parrallely
// with controller layer

public interface PropertiesService{

    Optional<Property> findById(Long id);

    Long create(String address,String ownerName,Float area,Integer rent);

    List<Property> findAll();
    
}