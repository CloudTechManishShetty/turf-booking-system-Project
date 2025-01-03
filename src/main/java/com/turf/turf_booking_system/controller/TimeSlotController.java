package com.turf.turf_booking_system.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turf.turf_booking_system.model.Slot;
import com.turf.turf_booking_system.service.slotService;

@RestController
@RequestMapping("/api/slots")
public class TimeSlotController {
    @Autowired
    private slotService slotservice;

    @PostMapping
    public ResponseEntity<Slot> createSlot(@RequestBody Slot slot) {
        Slot newSlot = slotservice.createSlot(slot);
        return ResponseEntity.ok(newSlot);
    }

    @GetMapping
    public ResponseEntity<List<Slot>> getSlotsByTurfAndDate(
        @RequestParam Long turfId, @RequestParam LocalDate date) {
        return ResponseEntity.ok(slotservice.getSlotsByTurfAndDate(turfId, date));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Slot> updateSlot(
        @PathVariable Long id, @RequestBody Slot slot) {
        Slot updatedSlot = slotservice.updateSlot(id, slot);
        return ResponseEntity.ok(updatedSlot);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long id) {
        slotservice.deleteSlot(id);
        return ResponseEntity.noContent().build();
    }
}
