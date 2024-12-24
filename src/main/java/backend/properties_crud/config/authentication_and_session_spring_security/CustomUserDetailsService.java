package backend.properties_crud.config.authentication_and_session_spring_security;

import backend.properties_crud.entity.User;
import backend.properties_crud.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User user = userRepository.findByEmail(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found with email: " + username);
    }

    List<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
    roles.add(new SimpleGrantedAuthority("USER"));

    // two objects of User are in script so absolute import written below to tell we are referring
    // to User of Spring Security
    org.springframework.security.core.userdetails.User u =
        new org.springframework.security.core.userdetails.User(
            user.getEmail(), user.getPassword(), roles);

    return u;
  }
}
