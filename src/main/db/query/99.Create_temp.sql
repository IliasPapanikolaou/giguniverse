-- Create table temp to load images

create table temp
(
    id         int auto_increment
        primary key,
    image      longblob null,
    concert_id int      null
);