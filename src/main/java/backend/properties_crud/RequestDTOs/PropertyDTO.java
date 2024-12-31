package backend.properties_crud.RequestDTOs;

import backend.properties_crud.entity.PropertyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PropertyDTO(
    @Schema(
            description = "Address of the property",
            required = true,
            example = "Ashok Complex,Pimpri Chinchwad,Pune.")
        @NotBlank(message = "Address cannot be empty")
        @Size(min = 20, max = 500)
        String address,
    @Schema(description = "Area of the property in square feet", required = true, example = "1000")
        @NotNull(message = "Area cannot be null")
        @Min(value = 100, message = "Area must be greater than 100 sqft")
        Integer area,
    @Schema(description = "Monthly rent of the property in INR", required = true, example = "1000")
        @NotNull(message = "Rent cannot be null")
        @Min(value = 1000, message = "Rent must be greater than 1000rs.")
        Integer rent,
    @Schema(
            description = "Type of the property",
            required = true,
            allowableValues = "ONE_BHK,ONE_RK,TWO_BHK,THREE_BHK,BUNGALOW")
        @NotNull(message = "Property type cannot be null")
        @Enumerated(EnumType.STRING)
        PropertyType type) {}
