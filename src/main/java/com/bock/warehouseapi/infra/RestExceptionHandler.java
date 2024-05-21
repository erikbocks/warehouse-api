package com.bock.warehouseapi.infra;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bock.warehouseapi.entities.dtos.ErrorMessageDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.utils.RestResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final RestResponse restResponse;

    public RestExceptionHandler(RestResponse restResponse) {
        this.restResponse = restResponse;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ErrorMessageDTO> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String message = error.getDefaultMessage();
            ErrorMessageDTO err = new ErrorMessageDTO(error.getField(), message);
            errors.add(err);
        });

        return restResponse.badRequest("Ocorreu um erro ao processar sua requisição.", errors);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        if (ex.getCause() instanceof InvalidFormatException exception) {
            List<ErrorMessageDTO> messages = new ArrayList<>();

            String value = exception.getValue().toString();
            String targetType = exception.getTargetType().getSimpleName();

            exception.getPath().forEach(field -> messages.add(new ErrorMessageDTO(field.getFieldName(), String.format("O valor %s deve ser do tipo %s", value, targetType))));

            return restResponse.badRequest("Ocorreu um erro com os dados do payload.", messages);
        }

        return restResponse.badRequest("Ocorreu um erro com o corpo da sua requisição. Verifique a documentação e tente novamente.");
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return restResponse.notFound("Nenhum recurso encontrado.");
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException exception) {
            List<ErrorMessageDTO> messages = new ArrayList<>();

            String targetType = Objects.requireNonNull(exception.getRequiredType()).getSimpleName();
            String propertyName = exception.getPropertyName();
            String value = Objects.requireNonNull(exception.getValue()).toString();

            messages.add(new ErrorMessageDTO(propertyName, String.format("O valor %s deve ser do tipo %s", value, targetType)));

            return restResponse.badRequest("Ocorreu um erro com a tipagem dos dados", messages);
        }

        return restResponse.badRequest("Ocorreu um erro com a tipagem dos dados.");
    }

    @ExceptionHandler(InvalidDataException.class)
    private ResponseEntity<Object> invalidDataHandler(InvalidDataException exception) {
        return restResponse.badRequest(exception.getMessage());
    }

    @ExceptionHandler(JWTVerificationException.class)
    private ResponseEntity<Object> handleJWTVerificationException(Exception ex) {
        return restResponse.unauthorized("Esse token é inválido.");
    }
}
