package backend.properties_crud.controllers;

import backend.properties_crud.RequestDTOs.PropertyDTO;
import backend.properties_crud.entity.Property;
import backend.properties_crud.entity.PropertyType;
import backend.properties_crud.entity.User;
import backend.properties_crud.repository.PropertyRepository;
import backend.properties_crud.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/property")
@RequiredArgsConstructor
public class PropertyCRUD {

  private final PropertyRepository propertyRepository;
  private final UserRepository userRepository;

  @PostMapping("/new")
  public ResponseEntity<?> store_property(
      @Valid @RequestBody PropertyDTO property, @AuthenticationPrincipal UserDetails userDetails) {

    User user = userRepository.findByEmail(userDetails.getUsername());
    Property new_property =
        Property.builder()
            .address(property.address())
            .area(property.area())
            .rent(property.rent())
            .user(user)
            .type(property.type())
            .build();

    new_property = propertyRepository.save(new_property);

    user.getProperties().add(new_property);

    userRepository.save(user);

    Map<String, Object> response = new HashMap<>();
    response.put("message", "Property Listed.");
    response.put("data", new_property);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<?> get_all_properties(@AuthenticationPrincipal UserDetails userDetails) {
    User user = userRepository.findByEmail(userDetails.getUsername());
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Properties Retrieved.");
    response.put("data", user.getProperties());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> get_property_by_id(
      @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
    User user = userRepository.findByEmail(userDetails.getUsername());
    Property property = propertyRepository.findByIdAndUser(id, user);
    if (property == null) {
      return new ResponseEntity<>("Property not found.", HttpStatus.NOT_FOUND);
    }
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Property Retrieved.");
    response.put("data", property);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update_property(
      @PathVariable Long id,
      @Valid @RequestBody PropertyDTO property,
      @AuthenticationPrincipal UserDetails userDetails) {
    User user = userRepository.findByEmail(userDetails.getUsername());
    Property existing_property = propertyRepository.findByIdAndUser(id, user);
    if (existing_property == null) {
      return new ResponseEntity<>("Property not found.", HttpStatus.NOT_FOUND);
    }
    existing_property.setAddress(property.address());
    existing_property.setArea(property.area());
    existing_property.setRent(property.rent());
    existing_property.setType(property.type());
    propertyRepository.save(existing_property);
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Property Updated.");
    response.put("data", existing_property);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete_property(
      @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
    User user = userRepository.findByEmail(userDetails.getUsername());
    Property property = propertyRepository.findByIdAndUser(id, user);
    if (property == null) {
      return new ResponseEntity<>("Property not found.", HttpStatus.NOT_FOUND);
    }
    propertyRepository.delete(property);
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Property Deleted.");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/search")
  public ResponseEntity<?> search_properties(
      @RequestParam(required = false) String address,
      @RequestParam(required = false) PropertyType type,
      @RequestParam(required = false) Integer minRent,
      @RequestParam(required = false) Integer maxRent) {

    List<Property> properties = searchProperties(address, type, minRent, maxRent);

    Map<String, Object> response = new HashMap<>();
    response.put("message", "Properties Retrieved.");
    response.put("data", properties);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  private List<Property> searchProperties(
      String address, PropertyType type, Integer minRent, Integer maxRent) {
    if (address != null && type != null && minRent != null && maxRent != null) {
      return propertyRepository.findByTypeAndAddressContainingAndRentBetween(
          type, address, minRent, maxRent);
    } else if (type != null && minRent != null && maxRent != null) {
      return propertyRepository.findByTypeAndRentBetween(type, minRent, maxRent);
    } else if (address != null && minRent != null && maxRent != null) {
      return propertyRepository.findByAddressContainingAndRentBetween(address, minRent, maxRent);
    } else if (type != null && address != null) {
      return propertyRepository.findByTypeAndAddressContaining(type, address);
    } else if (type != null) {
      return propertyRepository.findByType(type);
    } else if (address != null) {
      return propertyRepository.findByAddressContaining(address);
    } else {
      return propertyRepository.findAll();
    }
  }
}
