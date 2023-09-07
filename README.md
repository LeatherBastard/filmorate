# java-filmorate
Repository for Filmorate project.

![Database scheme of Filmorate project](filmorate_DB.png)

SELECT title<br>
FROM film<br>
WHERE EXTRACT(YEAR FROM release_date)=2009;<br>
<br>
SELET COUNT(user_id) AS likes_count<br>
FROM like<br>
WHERE film_id=2;<br>
<br>
SELECT film_id,COUNT(user_id) AS likes_count<br>
FROM like<br> 
GROUP BY film_id<br>
ORDER BY likes_count DESC<br>
LIMIT 10;<br>  

