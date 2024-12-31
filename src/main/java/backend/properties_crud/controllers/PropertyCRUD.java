package backend.properties_crud.controllers;

import backend.properties_crud.RequestDTOs.PropertyDTO;
import backend.properties_crud.entity.Property;
import backend.properties_crud.entity.User;
import backend.properties_crud.repository.PropertyRepository;
import backend.properties_crud.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.validation.Valid;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/property")
@RequiredArgsConstructor
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class PropertyCRUD {

  private final PropertyRepository propertyRepository;
  private final UserRepository userRepository;

  @PostMapping("/new")
  @Operation(
      summary = "Create a new property listing",
      description = "Saves a new property for the authenticated user.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Property created successfully",
            content =
                @Content(
                    examples =
                        @ExampleObject(
                            value =
                                """
                  {
  "data": {
    "id": 1,
    "address": "Ashok Complex, Pimpri Chinchwad, Pune.",
    "area": 1200,
    "rent": 15000,
    "type": "ONE_BHK"
  },
  "message": "Property Listed."
}
"""))),
        @ApiResponse(
            responseCode = "400",
            description = "Validation error",
            content =
                @Content(
                    examples =
                        @ExampleObject(
                            value =
                                """
                                {
  "message": "address: size must be between 20 and 500"
}
"""))),
        @ApiResponse(
            responseCode = "302",
            description = "Property already exists",
            content =
                @Content(
                    examples =
                        @ExampleObject(
                            value =
                                """
      {
  "message": "Property already exists"
}
""")))
      })
  @SecurityRequirement(name = "basicAuth")
  public ResponseEntity<?> store_property(
      @Valid
          @RequestBody(
              description = "Property details to be created",
              content =
                  @Content(
                      examples =
                          @ExampleObject(
                              value =
                                  """
          {
  "address": "Ashok Complex, Pimpri Chinchwad, Pune.",
  "area": 1200,
  "rent": 15000,
  "type": "ONE_BHK"
}
""")))
          @org.springframework.web.bind.annotation.RequestBody
          PropertyDTO property,
      @Parameter(hidden = true) @AuthenticationPrincipal
          UserDetails userDetails) { // Hide UserDetails from swagger
    Map<String, Object> response = new HashMap<>();

    User user = userRepository.findByEmail(userDetails.getUsername());
    if (!propertyRepository.findByAddressContaining(property.address()).isEmpty()) {
      response.put("message", "Property already exists");
      return new ResponseEntity<>(response, HttpStatus.FOUND);
    }
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

    response.put("message", "Property Listed.");
    response.put("data", new_property);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping
  @Operation(
      summary = "Get all properties for the authenticated user",
      description = "Retrieves all property listings owned by the authenticated user.")
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
      "address": "Ashok Complex, Pimpri Chinchwad, Pune.",
      "area": 1200,
      "rent": 15000,
      "type": "ONE_BHK"
    },
    {
      "id": 2,
      "address": "Near Pimpri Chinchwad College of Engineering,Nigdi,Pune",
      "area": 960000,
      "rent": 1000000,
      "type": "BUNGALOW"
    }
  ],
  "message": "Properties Retrieved."
}
""")))
      })
  @SecurityRequirement(name = "basicAuth")
  public ResponseEntity<?> get_all_properties(
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
    User user = userRepository.findByEmail(userDetails.getUsername());
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Properties Retrieved.");
    response.put("data", user.getProperties());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get a property by ID",
      description = "Retrieves a specific property by its ID, accessible only by the owner.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Property retrieved successfully",
            content =
                @Content(
                    examples =
                        @ExampleObject(
                            value =
                                """
                  {
  "data": {
    "id": 2,
    "address": "Near Pimpri Chinchwad College of Engineering,Nigdi,Pune",
    "area": 960000,
    "rent": 1000000,
    "type": "BUNGALOW"
  },
  "message": "Property Retrieved."
}
"""))),
        @ApiResponse(
            responseCode = "404",
            description = "Property not found",
            content =
                @Content(
                    examples =
                        @ExampleObject(
                            value =
                                """
              {
  "message": "Property with given Id does not exist"
}
""")))
      })
  @SecurityRequirement(name = "basicAuth")
  public ResponseEntity<?> get_property_by_id(
      @Parameter(description = "Unique identifier of the property", required = true) @PathVariable
          Long id,
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
    User user = userRepository.findByEmail(userDetails.getUsername());
    Map<String, Object> response = new HashMap<>();
    Property property = propertyRepository.findByIdAndUser(id, user);
    if (property == null) {
      response.put("message", "Property with given Id does not exist");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    response.put("message", "Property Retrieved.");
    response.put("data", property);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update a property",
      description = "Updates an existing property listing owned by the authenticated user.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Property updated successfully",
            content =
                @Content(
                    examples =
                        @ExampleObject(
                            value =
                                """
                {
  "data": {
    "id": 1,
    "address": "Near Pimpri Chinchwad College of Engineering,Nigdi,Pune",
    "area": 9600000,
    "rent": 1000000,
    "type": "THREE_BHK"
  },
  "message": "Property Updated."
}
"""))),
        @ApiResponse(responseCode = "404", description = "Property not found")
      })
  @SecurityRequirement(name = "basicAuth")
  public ResponseEntity<?> update_property(
      @Parameter(description = "Unique identifier of the property to update", required = true)
          @PathVariable
          Long id,
      @Valid
          @RequestBody(
              description = "Updated property details",
              content =
                  @Content(
                      examples =
                          @ExampleObject(
                              value =
                                  """
            {
  "address": "Near Pimpri Chinchwad College of Engineering,Nigdi,Pune",
  "area": 9600000,
  "rent": 1000000,
  "type": "THREE_BHK"
}
""")))
          @org.springframework.web.bind.annotation.RequestBody
          PropertyDTO property,
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
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
  @Operation(
      summary = "Delete a property",
      description = "Deletes a property listing owned by the authenticated user.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Property deleted successfully",
            content =
                @Content(
                    examples =
                        @ExampleObject(
                            value =
                                """
            {
  "message": "Property Deleted."
}
"""))),
        @ApiResponse(responseCode = "404", description = "Property not found")
      })
  @SecurityRequirement(name = "basicAuth")
  public ResponseEntity<?> delete_property(
      @Parameter(description = "Unique identifier of the property to delete", required = true)
          @PathVariable
          Long id,
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
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
}
