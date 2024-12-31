package backend.properties_crud.RequestDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationAndUpdationDTO(

    @Schema(description = "First name of the user", example = "John")
    @Size(min = 3, max = 50, message = "First Name must be between 3 to 50 characters")
    String firstName,

    @Schema(description = "Last name of the user", example = "Doe")
    @Size(min = 3, max = 50, message = "Last Name must be between 3 to 50 characters")
    String lastName,

    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    String email,

    @Schema(description = "Password for the user (between 8 and 12 characters)", example = "StrongPass123")
    @Size(min = 8, max = 12, message = "Password must be between 8 to 12 characters")
    String password,

    @Schema(description = "Phone number of the user (exactly 10 digits)", example = "1234567890")
    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
    String phone

) {
}