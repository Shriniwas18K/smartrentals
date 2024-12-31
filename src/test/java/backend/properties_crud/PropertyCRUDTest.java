package backend.properties_crud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import backend.properties_crud.RequestDTOs.PropertyDTO;
import backend.properties_crud.controllers.PropertyCRUD;
import backend.properties_crud.entity.Property;
import backend.properties_crud.entity.PropertyType;
import backend.properties_crud.entity.User;
import backend.properties_crud.repository.PropertyRepository;
import backend.properties_crud.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
public class PropertyCRUDTest {

  /* This test assumes user is already registered and always sends valid
   * http basic authentication header
   */

  @Mock private PropertyRepository propertyRepository;

  @Mock private UserRepository userRepository;

  @InjectMocks private PropertyCRUD propertyCRUD;

  UserDetails userDetails=new UserDetails() {
    public String getUsername(){
        return "user@example.com";
    }
    public String getPassword(){
        return "temp";
    }
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return null;
    }
  };
  @Test
  public void testStoreProperty() {
    // Arrange
    PropertyDTO propertyDTO = new PropertyDTO("address", 100, 1000, PropertyType.ONE_BHK);
    User user = User.builder().email("user@example.com").password("temp").properties(new ArrayList<Property>()).build();
    when(userRepository.findByEmail("user@example.com")).thenReturn(user);
    when(propertyRepository.save(any(Property.class))).thenReturn(new Property());

    // Act
    ResponseEntity<?> response = propertyCRUD.store_property(propertyDTO, userDetails);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    verify(propertyRepository, times(1)).save(any(Property.class));
  }

  @Test
  public void testGetPropertyById() {
    // Arrange
    User user = User.builder().email("user@example.com").password("temp").build();
    Long id = 1L;

    Property property = new Property();
    when(userRepository.findByEmail("user@example.com")).thenReturn(user);
    when(propertyRepository.findByIdAndUser(id, user)).thenReturn(property);

    // Act
    ResponseEntity<?> response = propertyCRUD.get_property_by_id(id, userDetails);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(propertyRepository, times(1)).findByIdAndUser(id, user);
  }

  @Test
  public void testUpdateProperty() {
    // Arrange
    Long id = 1L;
    PropertyDTO propertyDTO = new PropertyDTO("address", 100, 1000, PropertyType.ONE_BHK);
    User user = User.builder().email("user@example.com").password("temp").build();
    Property property = new Property();
    when(userRepository.findByEmail("user@example.com")).thenReturn(user);
    when(propertyRepository.findByIdAndUser(id, user)).thenReturn(property);

    // Act
    ResponseEntity<?> response = propertyCRUD.update_property(id, propertyDTO, userDetails);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(propertyRepository, times(1)).save(any(Property.class));
  }

  @Test
  public void testDeleteProperty() {
    // Arrange
    Long id = 1L;
    User user = User.builder().email("user@example.com").password("temp").build();
    Property property = new Property();
    when(userRepository.findByEmail("user@example.com")).thenReturn(user);
    when(propertyRepository.findByIdAndUser(id, user)).thenReturn(property);

    // Act
    ResponseEntity<?> response = propertyCRUD.delete_property(id, userDetails);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(propertyRepository, times(1)).delete(property);
  }

  @Test
  public void testSearchProperties() {
    // Arrange
    List<Property> properties = new ArrayList<>();
    properties.add(new Property());
    when(propertyRepository.findByTypeAndAddressContainingAndRentBetween(
            any(), any(), any(), any()))
        .thenReturn(properties);

    // Act
    ResponseEntity<?> response =
        propertyCRUD.search_properties("address", PropertyType.ONE_BHK, 1000, 2000);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(propertyRepository, times(1))
        .findByTypeAndAddressContainingAndRentBetween(any(), any(), any(), any());
  }
}
