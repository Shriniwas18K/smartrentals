package backend.properties_crud.controllers;

import backend.properties_crud.RequestDTOs.UserRegistrationAndUpdationDTO;
import backend.properties_crud.entity.User;
import backend.properties_crud.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GreetingsAndUserRegistration {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/registration")
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
    // conventionally password is not setn in response
    Map<String, String> userprofile = new HashMap<>();
    userprofile.put("firstName", user.getFirstName());
    userprofile.put("lastName", user.getLastName());
    userprofile.put("email", user.getEmail());
    userprofile.put("phone", user.getPhone());
    response.put("data", userprofile);
    
    return new ResponseEntity<>(response,HttpStatus.CREATED);
  }

  /* This below endpoint is written to test after user is registered */
  @GetMapping("/")
  @ResponseStatus(code = HttpStatus.OK)
  public ResponseEntity<?> greetings() {
    Map<String, String> response = new HashMap<>();
    response.put("message", "Greetings!! Glad to see you here");
    return ResponseEntity.ok(response);
  }
}
