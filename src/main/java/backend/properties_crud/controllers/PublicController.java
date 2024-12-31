package backend.properties_crud.controllers;

import backend.properties_crud.RequestDTOs.UserRegistrationAndUpdationDTO;
import backend.properties_crud.entity.Property;
import backend.properties_crud.entity.PropertyType;
import backend.properties_crud.entity.User;
import backend.properties_crud.repository.PropertyRepository;
import backend.properties_crud.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(
    name = "Public Endpoints",
    description =
        "Endpoints for searching properties publicly and registration.These endpoints dont require"
            + " authentication.")
public class PublicController {

  private final PropertyRepository propertyRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/registration")
  @Operation(summary = "User Registration", description = "Registers a new user.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = " Registration Successful.",
            content =
                @Content(
                    examples =
                        @ExampleObject(
                            value =
                                """
    {
  "data": {
    "firstName": "storage",
    "lastName": "decentralized",
    "phone": "1234567890",
    "email": "storagedecentralized@gmail.com",
    "properties": null
  },
  "message": "Registration Successful."
}
"""))),
        @ApiResponse(
            responseCode = "422",
            description = "Validation error. Such kind of errors are given for better explanation.",
            content =
                @Content(
                    examples =
                        @ExampleObject(
                            value =
                                """
                {
  "message": "password: Password must be between 8 to 12 characters"
}
"""))),
        @ApiResponse(
            responseCode = "302",
            description = "If user already exists then",
            content =
                @Content(
                    examples =
                        @ExampleObject(
                            value =
                                """
              {
  "message": "User already exists with this email."
}
""")))
      })
  public ResponseEntity<?> userRegistrationHandler(
      @Valid
          @RequestBody
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "User registration details",
              content =
                  @Content(
                      examples =
                          @ExampleObject(
                              value =
                                  """
                      {
  "firstName": "storage",
  "lastName": "decentralized",
  "email": "storagedecentralized@gmail.com",
  "password": "123456789",
  "phone": "1234567890"
}
""")))
          UserRegistrationAndUpdationDTO DTO) {

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
  @Operation(
      summary = "Search Properties",
      description =
          "Searches for properties based on various criteria. This endpoint does not require"
              + " authentication.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Properties retrieved successfully",
            content =
                @Content(
                    examples =
                        @ExampleObject(
                            value =
                                """
                  {
  "data": [
    {
      "id": 1,
      "address": "Near Pimpri Chinchwad College of Engineering,Nigdi,Pune",
      "area": 960000,
      "rent": 1000000,
      "type": "BUNGALOW"
    },
    {
      "id": 2,
      "address": "Near Bus Stand Nigdi,Pune",
      "area": 9600,
      "rent": 10000,
      "type": "TWO_BHK"
    }
  ],
  "message": "Properties Retrieved."
}
""")))
      })
  public ResponseEntity<?> search_properties(
      @Parameter(description = "Property address (optional)", style = ParameterStyle.FORM)
          @RequestParam(required = false)
          String address,
      @Parameter(description = "Property type (optional)", style = ParameterStyle.FORM)
          @RequestParam(required = false)
          PropertyType type,
      @Parameter(description = "Minimum rent (optional)", style = ParameterStyle.FORM)
          @RequestParam(required = false)
          Integer minRent,
      @Parameter(description = "Maximum rent (optional)", style = ParameterStyle.FORM)
          @RequestParam(required = false)
          Integer maxRent) {

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
