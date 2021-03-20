-- Create base64 from image loaded in temp table

update concert
inner join temp on temp.concert_id=concert.concert_id
set concert.image= to_base64(temp.image)
where temp.concert_id=concert.concert_id


--COMMIT;
