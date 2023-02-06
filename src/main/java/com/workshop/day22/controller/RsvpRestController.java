package com.workshop.day22.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.day22.model.Rsvp;
import com.workshop.day22.repo.RsvpRepo;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@RestController
@RequestMapping(path="/api")
public class RsvpRestController {
    
    @Autowired
    RsvpRepo rsvpRepo;

    // Task 2a: Get all Rsvps from MySQL
    @GetMapping(path="/rsvps", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllRsvps() {
        System.out.println("getting all Rsvps");
        List<Rsvp> rsvps = rsvpRepo.getAllRsvp();

        // create an array in json format
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (Rsvp r : rsvps) {
            // adding object to array
            arrBuilder.add(r.toJSON());
            
        }
        // return response entity if not empty
        if (rsvps.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{'error_code': '" + HttpStatus.NOT_FOUND + "'}");
        } else {
            // building ResponseEntity with status, contentType, body
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(arrBuilder.build().toString());
        }
    }

    // Task 2b: Get Rsvp by name
    @GetMapping(path="/rsvp", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRsvpByName(@RequestParam("q") String name) {
        Rsvp result = rsvpRepo.getRsvpByName(name);
        System.out.println("Result: " + result);
        if (null != result) {
            String obj = result.toJSON().toString();
            return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(obj);
        } else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{'error_code': '" + HttpStatus.NOT_FOUND + "'}");
        }

        
    }

}
