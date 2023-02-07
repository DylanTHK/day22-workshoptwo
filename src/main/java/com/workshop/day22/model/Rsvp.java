package com.workshop.day22.model;

import java.util.Date;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Rsvp {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private Date confirmationDate;
    private String comments;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Date getConfirmationDate() {
        return confirmationDate;
    }
    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    @Override
    public String toString() {
        return "Rsvp [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", confirmationDate="
                + confirmationDate + ", comments=" + comments + "]";
    }

    // method 1: SqlRowSet => Rsvp obj
    public static Rsvp create(SqlRowSet rs) {
        Rsvp rsvp = new Rsvp();
        rsvp.setId(rs.getInt("id"));
        rsvp.setName(rs.getString("rsvp_name"));
        rsvp.setEmail(rs.getString("email"));
        rsvp.setPhone(rs.getString("phone"));
        rsvp.setConfirmationDate(rs.getDate("confirmation_date"));
        rsvp.setComments(rs.getString("comments"));

        return rsvp;
    }

    // method 2: String => Rsvp Obj
    public static Rsvp createObj(String jsonString) throws ParseException {
        System.out.println("jsonString inside createObj: " + jsonString);

        JsonReader reader = Json.createReader(
            new ByteArrayInputStream(jsonString.getBytes()));

        System.out.println("JsonReader: " + reader);

        JsonObject obj = reader.readObject();
        Rsvp r = convertJObject(obj); 
        System.out.println("Rsvp Object in createObj: " + r);
        return r;
        // return null;
    }

    // method 2.1: JsonObject => Rsvp
    public static Rsvp convertJObject(JsonObject jObj) throws ParseException {
        final Rsvp rsvp = new Rsvp();
        rsvp.setName(jObj.getString("name"));
        rsvp.setEmail(jObj.getString("email"));
        rsvp.setPhone(jObj.getString("phone"));
        rsvp.setConfirmationDate(new SimpleDateFormat("yyyy-MM-dd").parse(jObj.getString("confirmation_date")));
        rsvp.setComments(jObj.getString("comments"));
        return rsvp;
    }

    // this.Rsvp => Json Object
    public JsonObject toJSON() {
        JsonObject jObject = Json.createObjectBuilder()
                .add("id", getId())
                .add("name", getName())
                .add("phone", getPhone())
                .add("confirmation_date", getConfirmationDate() != null ? getConfirmationDate().toString() : "")
                .add("commments", getComments() != null ? getComments() : "")
                .build();

        return jObject;
    }

    
}
