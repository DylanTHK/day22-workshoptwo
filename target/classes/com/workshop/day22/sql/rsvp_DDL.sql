-- create new db for workshop 22
create database ws22;

use ws22;
SET SQL_SAFE_UPDATES = 0;

-- create rsvp table (table for users to store booking details for events)
create table rsvp (
	id int not null auto_increment,
    rsvp_name varchar(128) not null,
    email varchar(128) not null,
    phone varchar(8) not null,
    confirmation_date date not null,
    comments varchar(255),
    primary key(id)
);

-- task 2a) select all from rsvp
select * from rsvp;

-- task 2b) select by rsvp name
select * from rsvp where rsvp_name LIKE CONCAT('%','Bob','%');
select * from rsvp where rsvp_name LIKE '%Bob%';

-- task 2c) insert rsvp from form
insert into rsvp (rsvp_name, email, phone, confirmation_date, comments) 
values ("Bob", "bob@email.com", "99999999", "2023-01-02", "comment body");

-- task 2d) update existing rsvp
update rsvp
set rsvp_name='Bobby', email='bob@email.com', phone='11111111', confirmation_date='1990-01-01', comments='No comments'
where email = 'bob@email.com';
select * from rsvp;

-- task 2e) count number of rsvps
select count(*) from rsvp;

-- get ID by email
select id from rsvp where email = 'freddie@g.com';
