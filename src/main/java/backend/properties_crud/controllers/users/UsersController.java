package backend.properties_crud.controllers.users;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.properties_crud.services.users.UserService;

@RestController
@RequestMapping("/")
public class UsersController{

    private final UserService userService;

    public UsersController(UserService userService){
        super();
        this.userService=userService;
    }

    @PostMapping("register")
    public ResponseEntity<?> createUser(
        @RequestParam("firstName") String firstName,
        @RequestParam("lastName") String lastName,
        @RequestParam("phone") String phone,
        @RequestParam("email") String email,
        @RequestParam("password") String password

    ){
        // we cannot use persistence layer stuff in Controllers
        // hence use service layer create method in interface
        // PropertiesService
        Long userId=userService.create(firstName,lastName,phone,email,password);
        Map<String, Object> response = new HashMap<>();
        response.put("id",userId.toString());
        response.put("message", "User created successfully with above id");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}