package backend.properties_crud.controllers;

import backend.properties_crud.RequestDTOs.UserRegistrationAndUpdationDTO;
import backend.properties_crud.entity.User;
import backend.properties_crud.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User Management", description = "Endpoints for managing users")
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class UserProfileAndRemoveAndUpdate {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @GetMapping("/")
  @ResponseStatus(code = HttpStatus.OK)
  @Operation(
      summary = "Greetings",
      description =
          "Simple greeting message. This endpoint can be accessed only by authenticated users. This"
              + " was made for purpose to test wheter user is able to access endpoints once he is"
              + " registered and is authenticated.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Greetings message",
            content =
                @Content(
                    examples =
                        @ExampleObject(
                            value =
                                """
          {
  "message": "Greetings!! Glad to see you here"
}
""")))
      })
  public ResponseEntity<?> greetings() {
    Map<String, String> response = new HashMap<>();
    response.put("message", "Greetings!! Glad to see you here");
    return ResponseEntity.ok(response);
  }

  @GetMapping("/profile")
  @ResponseStatus(HttpStatus.OK) // Corrected to OK
  @Operation(
      summary = "Get User Profile",
      description = "Retrieves the current user's profile information.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "User retrieved successfully",
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
    "properties": []
  },
  "message": "User retrieved."
}
""")))
      })
  @SecurityRequirement(name = "basicAuth")
  public ResponseEntity<?> get_user_profile(
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
    User user = userRepository.findByEmail(userDetails.getUsername());
    Map<String, Object> response = new HashMap<>();
    response.put("message", "User retrieved.");
    response.put("data", user);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("/update")
  @Operation(
      summary = "Update User Profile",
      description = "Updates the current user's profile information.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "422",
            description = "Validation Error",
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
            responseCode = "202",
            description = "If password was Updated",
            content =
                @Content(
                    examples =
                        @ExampleObject(
                            value =
                                """
            {
  "data": {
    "firstName": "storage",
    "lastName": "decent",
    "phone": "1234567890",
    "email": "storagedecentralized@gmail.com",
    "properties": []
  },
  "NOTE": "As password is updated hence now onwards each request should contain updated password in the authorization header.",
  "message": "User details updated."
}
""")))
      })
  @SecurityRequirement(name = "basicAuth")
  public ResponseEntity<?> update_user(
      @Valid
          @RequestBody
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              content =
                  @Content(
                      examples =
                          @ExampleObject(
                              value =
                                  """
                {
  "firstName": "storage",
  "lastName": "decent",
  "email": "storagedecentralized@gmail.com",
  "password": "123456789",
  "phone": "1234567890"
}
""")))
          UserRegistrationAndUpdationDTO DTO,
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
    Map<String, Object> response = new HashMap<>();

    User user = userRepository.findByEmail(userDetails.getUsername());
    user.setEmail(DTO.email());
    user.setFirstName(DTO.firstName());
    user.setLastName(DTO.lastName());
    if (!passwordEncoder.matches(DTO.password(), user.getPassword())) { // Correct password check
      user.setPassword(passwordEncoder.encode(DTO.password()));
      response.put(
          "NOTE",
          "As password is updated hence now onwards each request should contain updated password"
              + " in the authorization header.");
    }
    user.setPhone(DTO.phone());

    userRepository.save(user);

    response.put("message", "User details updated.");
    response.put("data", user);
    return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
  }

  @DeleteMapping("/delete")
  @Transactional
  @Operation(
      summary = "Delete User Account",
      description = "Deletes the current user's account and associated properties.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "202",
            description = "User and associated properties removed",
            content =
                @Content(
                    examples =
                        @ExampleObject(
                            value =
                                """
            {
  "message": "User and associated properties removed."
}
""")))
      })
  @SecurityRequirement(name = "basicAuth")
  public ResponseEntity<Map<String, String>> deleteUser(
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
    String email = userDetails.getUsername();
    userRepository.deleteByEmail(email);
    Map<String, String> response = new HashMap<>();
    response.put("message", "User and associated properties removed.");
    return ResponseEntity.accepted().body(response);
  }
}
