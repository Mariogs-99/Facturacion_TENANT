package sv.gov.cnr.cnrpos.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExcepcionHandler {
    @ExceptionHandler(TransaccionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> manejoErrorDeDominio(TransaccionException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> manejoResponseBodyErroneo(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> manejarEroresDeValidacion(MethodArgumentNotValidException ex) {
//        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(FieldError::toString).collect(Collectors.toList());
        errors.add(ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()).toString());
        errors.add(ex.getBindingResult().getFieldErrors().stream().map(FieldError::getField).collect(Collectors.toList()).toString());
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errores De Validacion", errors);
        return errorResponse;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> manejoExcepcionesGenerales(Exception ex) {
        log.error(ex.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error interno del servidor. Mensaje: %s".formatted(ex.getMessage())));
    }

    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> manejoErrorDeAccesoDatos(InvalidDataAccessResourceUsageException ex) {
        log.error("Error de acceso a datos: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error interno del servidor. Mensaje: Problema de acceso a datos."));
    }

//    @ExceptionHandler(InvalidTokenException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ResponseEntity<ErrorResponse> handleInvalidTokenException(String ex) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("token sex"));
//    }
//
//    @ExceptionHandler(ExpiredJwtException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
//        String errorMessage = "El token JWT ha caducado. ";
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(errorMessage));
//    }

}
