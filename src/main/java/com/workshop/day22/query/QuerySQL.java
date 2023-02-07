package com.workshop.day22.query;

public class QuerySQL {
    public static final String GET_ALL_SQL = "select * from rsvp";
    
    public static final String GET_BY_NAME_SQL = "select * from rsvp where rsvp_name LIKE CONCAT('%',?,'%')";

    public static final String INSERT_RSVP_SQL = """
        insert into rsvp (rsvp_name, email, phone, confirmation_date, comments) 
        values (?, ?, ?, ?, ?)""";

    public static final String UPDATE_RSVP_SQL = """
        update rsvp
        set rsvp_name=?, phone=?, confirmation_date=?, comments=?
        where email = ?;
        select * from rsvp;""";

    public static final String COUNT_RSVP_SQL = "select count(*) as cnt from rsvp";

    public static final String GET_ID_BY_EMAIL_SQL = "select id from rsvp where email=?";
}
