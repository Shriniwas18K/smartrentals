package backend.properties_crud.RequestDTOs;

import backend.properties_crud.entity.PropertyType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record PropertyDTO(
String address,
Integer area,
Integer rent,
@Enumerated(EnumType.STRING)
PropertyType type){}