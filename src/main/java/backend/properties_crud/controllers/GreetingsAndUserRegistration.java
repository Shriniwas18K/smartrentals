package backend.properties_crud.controllers;

import backend.properties_crud.DTOs.UserProfileResponseDTO;
import backend.properties_crud.DTOs.UserRegistrationAndUpdationDTO;
import backend.properties_crud.services.UserServiceImpl;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GreetingsAndUserRegistration {

  private final UserServiceImpl userServiceImpl;

  @PostMapping("/registration")
  @ResponseStatus(code = HttpStatus.CREATED)
  public UserProfileResponseDTO userRegistrationHandler(
      @Valid @RequestBody UserRegistrationAndUpdationDTO userRegistrationAndUpdationDTO) {
    return userServiceImpl.registerUser(userRegistrationAndUpdationDTO);
  }

  /* This below endpoint is written to test after user is registered */
  @GetMapping("/")
  @ResponseStatus(code = HttpStatus.OK)
  public ResponseEntity<?> greetings() {
    Map<String, String> response = new HashMap<>();
    response.put(
        "message",
        "Greetings!! Glad to see you here");
    return ResponseEntity.ok(response);
  }
}
