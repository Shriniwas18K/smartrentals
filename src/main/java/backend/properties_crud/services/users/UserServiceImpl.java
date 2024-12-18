package backend.properties_crud.services.users;

import java.util.ArrayList;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import backend.properties_crud.entity.User;
import backend.properties_crud.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public Long create(String firstName,String lastName,String phone,
    String email,String password){
        User user=User.builder()
            .firstName(firstName)
            .lastName(lastName)
            .phone(phone)
            .email(email)
            .password(password)
            .build();
        user=userRepository.save(user);
        return user.getId();
    }    

    // these below two methods are used by spring security to login

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user=userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email , check password or email , or pls register " + username);
        }
        // two objects of User are in script so absolute import written below to tell we are referring to User of Spring Security
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),new ArrayList<SimpleGrantedAuthority>());
    }
}