package br.edu.iff.ccc.bsi.dpskt_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiRestController extends ResponseEntityExceptionHandler {

  @ExceptionHandler(PendingClockExistsException.class)
  public ProblemDetail handlePendingClockExistsException(PendingClockExistsException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    problemDetail.setTitle("Pending Clock Exists");
    return problemDetail;
  }

  @ExceptionHandler(ClockNotFoundException.class)
  public ProblemDetail handleClockNotFoundException(ClockNotFoundException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    problemDetail.setTitle("Clock Not Found");
    return problemDetail;
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ProblemDetail handleUserNotFoundException(UserNotFoundException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    problemDetail.setTitle("User Not Found");
    return problemDetail;
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ProblemDetail handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    problemDetail.setTitle("User Already Exists");
    return problemDetail;
  }

}