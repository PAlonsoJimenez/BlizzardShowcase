package demo.weblayer.restexceptions;

import demo.servicelayer.exceptions.CatalogueException;
import demo.servicelayer.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(value = {CatalogueException.class})
    public ResponseEntity<Object> catalogueException(CatalogueException ex) {
        ErrorResponse errorResponse = createErrorResponse(ex);
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(value = {UserException.class})
    public ResponseEntity<Object> userException(UserException ex) {
        ErrorResponse errorResponse = createErrorResponse(ex);
        return buildResponseEntity(errorResponse);
    }

    private ErrorResponse createErrorResponse(CatalogueException ex) {
        return switch (ex.getCatalogueExceptionType()) {
            case BAD_AUTHORIZATION ->
                    new ErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getLocalizedMessage());
            case INVALID_INPUT -> new ErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getLocalizedMessage());
            case BEER_NOT_FOUND, MANUFACTURER_NOT_FOUND ->
                    new ErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getLocalizedMessage());
            default ->
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Mishandled internal server error", ex.getLocalizedMessage());
        };
    }

    private ErrorResponse createErrorResponse(UserException ex) {
        return switch (ex.getUserExceptionType()) {
            case BAD_AUTHENTICATION, UNAUTHORIZED ->
                    new ErrorResponse(HttpStatus.UNAUTHORIZED, "Bad authentication", ex.getLocalizedMessage());
            case FORBIDDEN -> new ErrorResponse(HttpStatus.FORBIDDEN, "Unauthorized", ex.getLocalizedMessage());
            case INVALID_INPUT -> new ErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getLocalizedMessage());
            default ->
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Mishandled internal server error", ex.getLocalizedMessage());
        };
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}
