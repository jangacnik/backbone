package com.resort.platform.backnode.auth.advice;

import com.resort.platform.backnode.auth.exceptions.UserAuthorizationException;
import com.resort.platform.backnode.foodtracker.exception.DepartmentAlreadyExistsException;
import com.resort.platform.backnode.foodtracker.exception.DepartmentNotFoundException;
import com.resort.platform.backnode.foodtracker.exception.InvalidRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class AuthorizationAdvice extends ResponseEntityExceptionHandler {
  @ExceptionHandler(value = {UserAuthorizationException.class})
  protected ResponseEntity<Object> handleUserAuthorizationException(RuntimeException ex,
      WebRequest webRequest) {
    return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN,
        webRequest);
  }

}
