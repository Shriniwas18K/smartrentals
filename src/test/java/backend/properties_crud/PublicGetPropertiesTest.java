package backend.properties_crud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.properties_crud.controllers.PublicController;
import backend.properties_crud.entity.Property;
import backend.properties_crud.entity.PropertyType;
import backend.properties_crud.repository.PropertyRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class PublicGetPropertiesTest {

  /* the public endpoints dont require authentication header */

  @InjectMocks private PublicController publicController;

  @Mock PropertyRepository propertyRepository;

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
        publicController.search_properties("address", PropertyType.ONE_BHK, 1000, 2000);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(propertyRepository, times(1))
        .findByTypeAndAddressContainingAndRentBetween(any(), any(), any(), any());
  }
}
