package backend.properties_crud.services.users;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{
    Long create(String firstName,String lastName,
    String phone,String email,String password);
}