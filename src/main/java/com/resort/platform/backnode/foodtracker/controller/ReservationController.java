package com.resort.platform.backnode.foodtracker.controller;

import com.resort.platform.backnode.foodtracker.model.MealReservation;
import com.resort.platform.backnode.foodtracker.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reserve")
@AllArgsConstructor
public class ReservationController {

    private ReservationService reservationService;

    @GetMapping("/{date}")
    public ResponseEntity<List<MealReservation>> getReservations(@PathVariable LocalDate date) {
        return ResponseEntity.ok(reservationService.getReservationsByDate(date));
    }

    @GetMapping
    public ResponseEntity<List<MealReservation>> getReservationsOfUserForCurrentMonth(@RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.ok(reservationService.getReservationsOfUser(token));
    }

    @PostMapping
    public ResponseEntity<Void> addReservation(@RequestBody MealReservation resevationRequest) {
        reservationService.addReservationForNextDay(resevationRequest);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteReservation(@RequestBody MealReservation reservationRequest) {
        reservationService.removeReservation(reservationRequest);
        return ResponseEntity.ok(null);
    }
}
