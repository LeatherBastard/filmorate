# java-filmorate
Repository for Filmorate project.

![Database scheme of Filmorate project](filmorate_db_schema.png)

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
<br>
SELECT  g.name AS genres<br>
FROM film_genres AS fm<br>
JOIN films AS f ON fm.film_id=f.film_id<br>
JOIN genres AS g ON fm.genre_id=g.genre_id <br>
WHERE f.film_id=1<br>
GROUP BY g.name;<br>
<br>
SELECT f.title AS film_title<br> 
FROM likes AS l<br>
JOIN films AS f ON l.film_id=f.film_id<br>
JOIN users AS u ON l.user_id=u.user_id<br>
WHERE l.user_id=1<br>
GROUP BY film_title;<br>





