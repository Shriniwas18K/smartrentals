package backend.properties_crud.services.users;

import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import backend.properties_crud.persistence.users.User;
import backend.properties_crud.persistence.users.UserRepository;
import backend.properties_crud.persistence.roles.Role;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.context.annotation.Bean;
import java.util.Arrays;


@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.encoder=new BCryptPasswordEncoder(12);
    }
    
    @Override
    public BCryptPasswordEncoder getEncoder(){
        return this.encoder;
    }

    @Override
    public Long create(String firstName,String lastName,String phone,
    String email,String password){
        User user=User.builder()
            .firstName(firstName)
            .lastName(lastName)
            .phone(phone)
            .email(email)
            .password(encoder.encode(password))
            .roles(Arrays.asList(new Role("ROLE_USER")))
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
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role->new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());
    }
}