package backend.properties_crud.controllers;

import backend.properties_crud.RequestDTOs.UserRegistrationAndUpdationDTO;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PublicController {

  private final PropertyRepository propertyRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @GetMapping("/")
  public ResponseEntity<?> greetings(){
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Greetings!! Have look at our docs https://github.com/Shriniwas18K/properties-crud/tree/main?tab=readme-ov-file#property-crud-operations-api");
    return ResponseEntity.ok(response);
  }

  @PostMapping(value="/registration",consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> userRegistrationHandler(
      @Valid @RequestBody UserRegistrationAndUpdationDTO DTO) {

    Map<String, Object> response = new HashMap<>();

    if (userRepository.findByEmail(DTO.email()) != null) {
      response.put("message", "User already exists with this email.");
      return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    final User user =
        User.builder()
            .firstName(DTO.firstName())
            .lastName(DTO.lastName())
            .phone(DTO.phone())
            .email(DTO.email())
            .password(passwordEncoder.encode(DTO.password()))
            .build();

    userRepository.save(user);

    response.put("message", "Registration Successful.");
    response.put("data", user);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
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