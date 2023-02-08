package com.workshop.day22.controller;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.day22.model.Rsvp;
import com.workshop.day22.repo.RsvpRepo;

import ch.qos.logback.core.net.SyslogOutputStream;
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

    // Task 2c: Insert new rsvp to table (if existing, overwrite)
    @PostMapping(path="/rsvp", 
        // consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> upsertRsvp(@RequestBody String json) throws ParseException {
        Rsvp rsvp = null;

        // creating java object from JsonString (handles any bad requests)
        try {
            rsvp = Rsvp.createObj(json);
        } catch (Exception e) {
            JsonObject resp = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(resp.toString());
        }
        System.out.println("Rsvp AFTER try: " + rsvp); // still no id

        // call insert (expecting boolean return?)
        Integer insertedId = rsvpRepo.insertRsvp(rsvp);
        System.out.println("Detected Id: "+ insertedId);

        JsonObject jsonObj = Json.createObjectBuilder()
                .add("id", insertedId).build();

        // build json response string with id
        return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .body(jsonObj.toString());
    }

    // Task 2d: update filtered row (with email) with new jsonString 
    @PutMapping(path="/rsvp")
    public ResponseEntity<String> updateExistingRsvp(@PathVariable String email, 
        @RequestBody String jsonString) throws ParseException {

        Rsvp rsvp = Rsvp.createObj(jsonString);
        // find rsvp to update with email
        Boolean result = rsvpRepo.updateRsvp(rsvp, email);
        // 
        if (result) {
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to update", HttpStatus.NOT_FOUND);
        }
    }

    // Task 2e: Get total rows in table
    @GetMapping(path="rsvps/count", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTotalCount() {
        Integer count = rsvpRepo.countRsvp();
        System.out.println("Total count: " + count);
        
        JsonObject jsonObj = Json.createObjectBuilder().
            add("count", count).build();

        if (count > 0) {
            return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObj.toString());
        } else {
            return new ResponseEntity<>("No Entries Found", HttpStatus.NOT_FOUND);
        }
        
    }

    // TODO Additional: Do batch update
    @PostMapping(path="/batch")
    public ResponseEntity<String> postBatchUpdate(@RequestBody String jsonArray) throws ParseException {
        System.out.println("Json Array: " + jsonArray);

        List<Rsvp> rsvpList = new LinkedList<>();
        // convert json array to java array
        rsvpList = rsvpRepo.convertStringtoArray(jsonArray);
        
        // pass java array to batchupdate method
        int added[] = rsvpRepo.batchInsert(rsvpList);
        
        System.out.println("Length of result: " + added.length);
        // return response entity with result of batch update
        
        JsonObject result = Json.createObjectBuilder()
            .add("Qty updated", added.length)
            .build();

        if (added.length > 0) {
            return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());
        } else {
            return new ResponseEntity<>("No entries added", 
                HttpStatus.EXPECTATION_FAILED);
        }
    }
    
}

