package com.workshop.day22.repo;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import static com.workshop.day22.query.QuerySQL.*;
import com.workshop.day22.model.Rsvp;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;


@Repository
public class RsvpRepo {
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    // method to get all information from db (return as List)
    public List<Rsvp> getAllRsvp() {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_ALL_SQL);
        List<Rsvp> listRsvp = new LinkedList<>();

        while (rs.next()) {
            Rsvp result = Rsvp.create(rs);
            listRsvp.add(result);
        }
        return listRsvp;
    }

    public Rsvp getRsvpByName(String name) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_BY_NAME_SQL, name);

        if (rs.next()) {
            Rsvp result = Rsvp.create(rs);
            return result;
        } else {
            return null;
        }
    }

    // TODO insertRSVP
    public Integer insertRsvp(Rsvp rsvp) {
        Integer rsvpId = 0;
        // if successfully inserted, retrieve id from mySQL
        Integer result = jdbcTemplate.update(INSERT_RSVP_SQL,
            rsvp.getName(), rsvp.getEmail(), 
            rsvp.getPhone(), 
            rsvp.getConfirmationDate(), 
            rsvp.getComments());
        
        System.out.println("inserted results: " + result);
        // else return 0
        if (result > 0) {
            // another method to get id by email and confirmation date
            SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_ID_BY_EMAIL_SQL, rsvp.getEmail());
            rs.next();
            rsvpId = rs.getInt("id");
        }

        return rsvpId;
    }

    // update table with new row of data
    public Boolean updateRsvp(Rsvp rsvp, String email) {
        Integer updated = jdbcTemplate.update(UPDATE_RSVP_SQL, rsvp.getName(), 
            rsvp.getPhone(),
            rsvp.getConfirmationDate(),
            rsvp.getComments(),
            email);

        return updated > 0;
    }

    // get total count for Rsvp
    public Integer countRsvp() {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(COUNT_RSVP_SQL);
        rs.next();
        Integer count = rs.getInt("cnt");

        return count;
    }

    // continue converting each json object to java object (rsvp)
    public List<Rsvp> convertStringtoArray(String jsonString) throws ParseException {
        JsonReader reader = Json.createReader(new ByteArrayInputStream(jsonString.getBytes()));

        JsonArray jArray = reader.readArray();
        
        List<Rsvp> rsvpList = new LinkedList<>();

        // System.out.println(jArray.getJsonObject(0));
        for(int i = 0; i < jArray.size(); i++) {
            Rsvp r = new Rsvp();
            JsonObject arrayObj = jArray.getJsonObject(i);
            r.setName(arrayObj.getString("name"));
            r.setPhone(arrayObj.getString("phone"));
            r.setEmail(arrayObj.getString("email"));

            r.setConfirmationDate(new SimpleDateFormat("yyyy-MM-dd")
                .parse(arrayObj.getString("confirmation_date")));
              
            r.setComments(null != arrayObj.getString("comments") ? arrayObj.getString("comments") : "");

            rsvpList.add(r);
            System.out.println("Added Rsvp to list: " + r);
        }

        return rsvpList;
    } 

    // TODO ()
    public int[] batchInsert(List<Rsvp> rsvpList) {
        List<Object[]> params = rsvpList.stream().map(r -> new Object[] {
            r.getName(), r.getEmail(), r.getPhone(), r.getConfirmationDate(), r.getComments()})
            .collect(Collectors.toList());
        System.out.println(params);
        // do batch update with list of objects
        int result[] = jdbcTemplate.batchUpdate(INSERT_RSVP_SQL, params);
        // return array of results
        return result;
    }


        


}
