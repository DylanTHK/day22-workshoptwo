package com.workshop.day22.repo;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import static com.workshop.day22.query.QuerySQL.*;
import com.workshop.day22.model.Rsvp;


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
}
