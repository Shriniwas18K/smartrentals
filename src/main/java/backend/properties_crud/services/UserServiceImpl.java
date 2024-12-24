package backend.properties_crud.services;

import backend.properties_crud.DTOs.UserProfileResponseDTO;
import backend.properties_crud.DTOs.UserRegistrationAndUpdationDTO;
import backend.properties_crud.entity.User;
import backend.properties_crud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl{

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserProfileResponseDTO registerUser(
      UserRegistrationAndUpdationDTO userRegistrationAndUpdationDTO) {
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
}
