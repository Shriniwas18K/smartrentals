package backend.properties_crud.controllers;

import backend.properties_crud.DTOs.UserProfileResponseDTO;
import backend.properties_crud.DTOs.UserRegistrationAndUpdationDTO;
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
  @ResponseStatus(code = HttpStatus.CREATED)
  public UserProfileResponseDTO userRegistrationHandler(
      @Valid @RequestBody UserRegistrationAndUpdationDTO userRegistrationAndUpdationDTO) {
    User user =
        User.builder()
            .firstName(userRegistrationAndUpdationDTO.firstName())
            .lastName(userRegistrationAndUpdationDTO.lastName())
            .phone(userRegistrationAndUpdationDTO.phoneNumber())
            .email(userRegistrationAndUpdationDTO.email())
            .password(passwordEncoder.encode(userRegistrationAndUpdationDTO.password()))
            .build();
    user = userRepository.save(user);
    return new UserProfileResponseDTO(
        user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone());
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
