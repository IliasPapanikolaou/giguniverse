-- Generate Random Data for Reservation

SET @MIN = '2021-05-01 00:00:00';
SET @MAX = '2022-12-31 00:00:00';

insert into reservation (final_date, starting_date, ticket_number, ticket_price, concert_concert_id, owner_user_id)
select TIMESTAMPADD(SECOND, FLOOR(RAND() * TIMESTAMPDIFF(SECOND, @MIN, @MAX)), @MIN) as final_date, -- Generate Random date
       now() as starting_date, -- Set Starting Date as now
       0 as ticket_number, -- Put 0 and Update later based on concert
       (select (floor(rand() * (20 - 4 + 1)))*5) as ticket_price, -- Generate random ticket price between 20 and 100 with %5=0!
       (SELECT concert_id FROM concert ORDER BY RAND() LIMIT 1) as concert_concert_id,  -- Get Random Concert id
        (SELECT user_id FROM user where dtype='Owner' ORDER BY RAND() LIMIT 1) as owner_user_id; -- Get Random Owner



-- Update ticket_number from capacity of venue_id
update reservation
inner join concert c on reservation.concert_concert_id = c.concert_id
inner join venue v on c.venue_venue_id = v.venue_id
set ticket_number=v.capacity;

-- Check capacity and ticket number
select v.capacity-ticket_number from reservation
inner join concert c on reservation.concert_concert_id = c.concert_id
inner join venue v on c.venue_venue_id = v.venue_id
where v.capacity-ticket_number<>0;