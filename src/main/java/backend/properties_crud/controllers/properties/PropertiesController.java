package backend.properties_crud.controllers.properties;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.beans.factory.annotation.Autowired;
import backend.properties_crud.services.properties.PropertiesService;
import backend.properties_crud.persistence.properties.Property;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// Controllers use only service layer

@RestController // this tells to return JSON responses using jackson underhood builtin
@RequestMapping("/api/properties")
public class PropertiesController{

    @Autowired
    private PropertiesService propertiesService;

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Property> getUser(@PathVariable Long id) {
        Property property = propertiesService.findById(id).orElse(null);
        if (property != null) {
            return ResponseEntity.ok(property);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get-all")
    public List<Property> getAllProperties(){
        return propertiesService.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProperty(
        @RequestParam("address") String address,
        @RequestParam("ownerName") String ownerName,
        @RequestParam("area") Float area,
        @RequestParam("rent") Integer rent
    ){
        // we cannot use persistence layer stuff in Controllers
        // hence use service layer create method in interface
        // PropertiesService
        Long propertyId=propertiesService.create(address,ownerName,area,rent);
        Map<String, Object> response = new HashMap<>();
        response.put("id",propertyId.toString());
        response.put("message", "Property saved successfully with above id in database");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}