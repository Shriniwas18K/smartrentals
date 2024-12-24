package backend.properties_crud.exceptions;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/*This error response object used by all errors make uniformity across responses */
@Data
@AllArgsConstructor
public class ErrorResponse {

    private int status;

    private String message;

    private List<String> errors;

}