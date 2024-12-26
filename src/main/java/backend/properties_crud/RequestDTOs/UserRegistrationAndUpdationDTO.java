package backend.properties_crud.RequestDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationAndUpdationDTO(
    
    @Size(min = 3, max = 50, message = "First Name must be between 3 to 50 characters")
    String firstName,

    @Size(min = 3, max = 50, message = "Last Name must be between 3 to 50 characters")
    String lastName,
    
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    String email,

    @Size(min = 8, max = 12, message = "Password must be between 8 to 12 characters")
    String password,

    @Size(min=10,max=10,message = "Phone number must be exactly 10 digits")
    String phone

) {}
