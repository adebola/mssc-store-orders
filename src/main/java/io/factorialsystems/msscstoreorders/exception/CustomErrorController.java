package io.factorialsystems.msscstoreorders.exception;

import io.factorialsystems.msscstoreorders.dto.MessageDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<MessageDTO> handleRuntimeException(RuntimeException rex) {
        final String message = String.format("%s caused by %s", rex.getMessage(), rex.getCause().getMessage());

        MessageDTO messageDTO = new MessageDTO(HttpStatus.BAD_REQUEST.value(), message);
        return ResponseEntity.badRequest().body(messageDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> handleBindErrors(MethodArgumentNotValidException exception) {

        final List<Map<String, String>> errorList = exception.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errorMap;
                }).toList();

        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler(TransactionSystemException.class)
    ResponseEntity<?> handleJPAViolations(TransactionSystemException tse) {
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.badRequest();

        if (tse.getCause().getCause() instanceof ConstraintViolationException cve) {
            List<Map<String, String>> errors = cve.getConstraintViolations().stream()
                    .map(cv -> {
                        Map<String, String> errMap = new HashMap<>();
                        errMap.put(cv.getPropertyPath().toString(), cv.getMessage());
                        return errMap;
                    }).toList();

            return bodyBuilder.body(errors);
        }

        return bodyBuilder.build();
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<MessageDTO> handleNotFoundException(NotFoundException nfe) {
        MessageDTO messageDTO = new MessageDTO(HttpStatus.NOT_FOUND.value(), nfe.getMessage());
        return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
    }
}
