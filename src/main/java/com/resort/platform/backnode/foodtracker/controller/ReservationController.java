package com.resort.platform.backnode.foodtracker.controller;

import com.resort.platform.backnode.foodtracker.model.MealReservation;
import com.resort.platform.backnode.foodtracker.service.ReservationService;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reserve")
@AllArgsConstructor
public class ReservationController {

  private ReservationService reservationService;

  /**
   * Get all reservations for given date
   *
   * @param date - date of the reservations
   * @return List of reservations for given date
   */
  @GetMapping("/{date}")
  public ResponseEntity<List<MealReservation>> getReservations(@PathVariable LocalDate date) {
    return ResponseEntity.ok(reservationService.getReservationsByDate(date));
  }

  /**
   * Returns list of all reservations of the user for the current month
   *
   * @param token - jwt token of logged in user
   * @return list of reservations
   */
  @GetMapping
  public ResponseEntity<List<MealReservation>> getReservationsOfUserForCurrentMonth(
      @RequestHeader(name = "Authorization") String token) {
    return ResponseEntity.ok(reservationService.getReservationsOfUser(token));
  }

  /**
   * Adds resevations for the next day
   *
   * @param resevationRequest - request object for reservation
   * @return OK if success
   */
  @PostMapping
  public ResponseEntity<Void> addReservation(@RequestBody MealReservation resevationRequest) {
    reservationService.addReservationForNextDay(resevationRequest);
    return ResponseEntity.ok(null);
  }

  /**
   * Removes the given reservation from the list
   *
   * @param reservationRequest - existing reservation object
   * @return OK if successful, throws exception if not found
   */
  @DeleteMapping
  public ResponseEntity<Void> deleteReservation(@RequestBody MealReservation reservationRequest) {
    reservationService.removeReservation(reservationRequest);
    return ResponseEntity.ok(null);
  }
}
