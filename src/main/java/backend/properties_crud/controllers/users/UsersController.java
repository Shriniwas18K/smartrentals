package backend.properties_crud.controllers.users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.properties_crud.services.users.UserService;

/* custom signin endpoint needs to be implemented */

@RestController
@RequestMapping("/")
public class UsersController{

    private final UserService userService;

    public UsersController(UserService userService){
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
        Long userId=userService.create(firstName,lastName,phone,email,password);
        return ResponseEntity.ok("registration successful");
    }
}