package com.resort.platform.backnode.foodtracker.controller;

import com.resort.platform.backnode.foodtracker.model.DailyMealReservations;
import com.resort.platform.backnode.foodtracker.model.rest.request.ReservationRequest;
import com.resort.platform.backnode.foodtracker.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reserve")
@AllArgsConstructor
public class ReservationController {

    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<DailyMealReservations> getReservations(@RequestBody ReservationRequest reservationRequest) {
        return ResponseEntity.ok(reservationService.getReservation(reservationRequest.getLocalDate()));
    }

    @PostMapping
    public ResponseEntity<Void> addReservation(@RequestBody ReservationRequest resevationRequest){
        reservationService.addReservationForNextDay(resevationRequest.getMealReservation());
        return ResponseEntity.ok(null);
    }
}
