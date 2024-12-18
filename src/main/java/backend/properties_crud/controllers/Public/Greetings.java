package backend.properties_crud.controllers.Public;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Greetings {
    @GetMapping("public")
    public ResponseEntity<?> welcome(){
        Map<String,String> response=new HashMap<>();
        response.put("message","""
            Greetings!! welcome to properties-crud apis ,have look at the docs at https://localhost:8080/docs
        """);
        return ResponseEntity.ok(response);
    }
}
