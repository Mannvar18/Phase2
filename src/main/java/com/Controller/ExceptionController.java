package com.Controller;



import com.exception.BookingAlreadyExists;
import com.exception.BookingAlreadyExistsForTeam;
import com.exception.DataNotFoundException;
import com.exception.InvalidDateException;
import com.exception.InvalidTimeException;
import com.exception.UserAlreadyExistsException;
import com.exception.UserNotFoundException;
import com.exception.WorkStationNotAvailable;
import com.exception.WorkstationNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



//@ExceptionHandler(value = WorkstationNotFoundException.class)
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = WorkstationNotFoundException.class)
    public ResponseEntity<Object> exception(WorkstationNotFoundException exception) {
    return new ResponseEntity<>("WorkStation not found", HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<Object> exception(DataNotFoundException exception) {
    return new ResponseEntity<>("Data missing", HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value= WorkStationNotAvailable.class)
    public ResponseEntity<Object> exception( WorkStationNotAvailable exception) {
        return new ResponseEntity<>("WorkStationNotAvailable", HttpStatus.BAD_REQUEST);
        }
    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<String> exception(UserNotFoundException exception) {
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

    @ExceptionHandler(value=UserAlreadyExistsException.class)
    public ResponseEntity<String> exception(UserAlreadyExistsException exception) {
        return new ResponseEntity<>("Sorry, you already have a booking on this date", HttpStatus.BAD_REQUEST);
        }
        @ExceptionHandler(value=InvalidDateException.class)
        public ResponseEntity<String> exception(InvalidDateException exception) {
            return new ResponseEntity<>("Invalid date or time", HttpStatus.BAD_REQUEST);
            }
    @ExceptionHandler(value=InvalidTimeException.class)
    public ResponseEntity<String> exception(InvalidTimeException exception) {
        return new ResponseEntity<>("Invalid Time", HttpStatus.BAD_REQUEST);
            
    }
        
    @ExceptionHandler(value=BookingAlreadyExists.class)
    public ResponseEntity<String> exception(BookingAlreadyExists exception) {
        return new ResponseEntity<>("You already have a booking for this time", HttpStatus.BAD_REQUEST);
            
    }
    
    
    @ExceptionHandler(value=BookingAlreadyExistsForTeam.class)
    public ResponseEntity<String> exception(BookingAlreadyExistsForTeam  exception) {
        return new ResponseEntity<>("The team has already a booking on this time", HttpStatus.BAD_REQUEST);
            
    }
    
 }