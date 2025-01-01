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
public class UserProfileAndRemoveAndUpdate {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @GetMapping("/")
  @ResponseStatus(code = HttpStatus.OK)
  public ResponseEntity<?> greetings() {
    Map<String, String> response = new HashMap<>();
    response.put("message", "Greetings!! Glad to see you here");
    return ResponseEntity.ok(response);
  }

  @GetMapping("/profile")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> get_user_profile(
      @AuthenticationPrincipal UserDetails userDetails) {
    User user = userRepository.findByEmail(userDetails.getUsername());
    Map<String, Object> response = new HashMap<>();
    response.put("message", "User retrieved.");
    response.put("data", user);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("/update")
  public ResponseEntity<?> update_user(
      @Valid @RequestBody UserRegistrationAndUpdationDTO DTO,
      @AuthenticationPrincipal UserDetails userDetails) {
    Map<String, Object> response = new HashMap<>();

    User user = userRepository.findByEmail(userDetails.getUsername());
    user.setEmail(DTO.email());
    user.setFirstName(DTO.firstName());
    user.setLastName(DTO.lastName());
    if (!passwordEncoder.matches(DTO.password(), user.getPassword())) {
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
  public ResponseEntity<Map<String, String>> deleteUser(
      @AuthenticationPrincipal UserDetails userDetails) {
    String email = userDetails.getUsername();
    userRepository.deleteByEmail(email);
    Map<String, String> response = new HashMap<>();
    response.put("message", "User and associated properties removed.");
    return ResponseEntity.accepted().body(response);
  }
}