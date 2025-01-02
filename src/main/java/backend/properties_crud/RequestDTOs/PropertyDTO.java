package backend.properties_crud.RequestDTOs;

import java.util.List;

import backend.properties_crud.entity.PropertyType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PropertyDTO(
        @NotBlank(message = "Address cannot be empty")
        @Size(min = 20, max = 500)
        String address,
        
        @NotNull(message = "Area cannot be null")
        @Min(value = 100, message = "Area must be greater than 100 sqft")
        Integer area,
        
        @NotNull(message = "Rent cannot be null")
        @Min(value = 1000, message = "Rent must be greater than 1000rs.")
        Integer rent,
        
        @NotNull(message = "Property type cannot be null")
        @Enumerated(EnumType.STRING)
        PropertyType type,
        
        List<String> base64EncodedImages) {}
