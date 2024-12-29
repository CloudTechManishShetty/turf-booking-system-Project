package com.turf.turf_booking_system.controller;

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

import com.turf.turf_booking_system.model.turfs;
import com.turf.turf_booking_system.service.turfService;

@RestController
@RequestMapping("/api/turfs")
public class TurfController {

    @Autowired
    turfService turfservice;
    public TurfController(turfService turfservice)
    {
        this.turfservice=turfservice;
    }
    
    @PostMapping
    public ResponseEntity<turfs> addTurf(@RequestBody turfs turf) {
        return ResponseEntity.ok(turfservice.addTurf(turf));
    }

    @PutMapping
    public String updateTurf(@RequestBody turfs turf) {
        turfservice.updateTurf(turf);
        return "Sucessfully Updated the Turf "+turf.getName();
    }

    @DeleteMapping("/{turfId}")
    public ResponseEntity<String> deleteTurf(@PathVariable Long turfId) {
        turfservice.deleteTurf(turfId);
        return ResponseEntity.ok("Turf deleted successfully");
    }

    @GetMapping
    public ResponseEntity<List<turfs>> listAllTurfs() {
        return ResponseEntity.ok(turfservice.listAllTurfs());
    }

    @GetMapping("/search")
    public ResponseEntity<List<turfs>> searchTurfs(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location) {
        return ResponseEntity.ok(turfservice.searchTurfs(name, location));
    }
}
