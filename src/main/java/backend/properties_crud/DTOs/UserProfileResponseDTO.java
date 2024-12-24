package backend.properties_crud.DTOs;

public record UserProfileResponseDTO(
    
    String firstName,

    String lastName,
    
    String email,

    String phoneNumber

) {}
