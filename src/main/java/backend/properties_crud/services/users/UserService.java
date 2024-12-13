package backend.properties_crud.services.users;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface UserService extends UserDetailsService{
    Long create(String firstName,String lastName,
    String phone,String email,String password);
    BCryptPasswordEncoder getEncoder();
}