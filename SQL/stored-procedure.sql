-- WORKING FUNCTIONS/PROCEDURES

DELIMITER $$ 
CREATE PROCEDURE add_movie (
        IN movie_title VARCHAR(100),
        IN movie_year INTEGER,
        IN movie_director VARCHAR(100),
        IN genre_name VARCHAR(32),
        IN star_name VARCHAR(100),
        IN star_year INTEGER,
        OUT movie_id VARCHAR(10),
        OUT message VARCHAR(500)	)
BEGIN
	DECLARE genre_id INT;
    DECLARE star_id VARCHAR(10);
    DECLARE star_message VARCHAR(100);
    DECLARE genre_message VARCHAR(100);

    SELECT find_movie_id(movie_title, movie_year, movie_director) INTO movie_id;
    IF movie_id IS NULL THEN 
		SELECT get_next_movie_id() INTO movie_id; 
        INSERT INTO movies(id, title, year, director)
			VALUES (movie_id, movie_title, movie_year, movie_director);
		INSERT INTO ratings(movieId, rating, numVotes) 
			VALUES (movie_id, 0, 0);
		
        IF genre_name IS NOT NULL THEN
			CALL add_genre(genre_name, genre_id, genre_message);
            CALL link_genre_to_movie(genre_id, movie_id);
		END IF;
        
        IF star_name IS NOT NULL THEN
			CALL add_star(star_name, star_year, star_id, star_message);
			CALL link_star_to_movie(star_id, movie_id);
		END IF;
        
		SET message = CONCAT(COALESCE(genre_message, ''), COALESCE(star_message, ''), ". New movie inserted. Movie ID: ", movie_id);
	ELSE
		SET message = CONCAT('Movie already in database. Movie ID: ', movie_id);
	END IF;
    
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE add_star(
	IN star_name VARCHAR(100),
    IN star_year INT,
    OUT star_id VARCHAR(10),
    OUT message VARCHAR(100)	)
BEGIN
	SELECT check_star_name(star_name) INTO star_id;
    IF star_id IS NULL THEN
		SELECT get_next_star_id() INTO star_id;
		INSERT INTO stars(id, name, birthYear)
			VALUES(star_id, star_name, NULL);
		SET message = CONCAT('. New star inserted. Star ID: ', star_id);
	ELSE
		IF star_year IS NOT NULL THEN
			SELECT get_next_star_id() INTO star_id;
			INSERT INTO stars(id, name, birthYear)
				VALUES(star_id, star_name, star_year);
			SET message = CONCAT('. New star inserted. Star ID: ', star_id);
		ELSE
			SET message = CONCAT('. Star already in database. Star ID: ', star_id);
		END IF;
	END IF;
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION check_star_name(star_name VARCHAR(100))
RETURNS VARCHAR(10)
READS SQL DATA
BEGIN
	DECLARE star_id VARCHAR(10);
    
	SELECT id
	INTO star_id
	FROM stars
	WHERE name = star_name
	LIMIT 1;
    
    RETURN star_id;    
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION find_movie_id(
		mTitle VARCHAR(100),
        mYear INTEGER,
        mDirector VARCHAR(100) )
RETURNS VARCHAR(10)
READS SQL DATA
BEGIN
	DECLARE movie_id VARCHAR(10);
	SELECT id
    INTO movie_id
    FROM movies
    WHERE title = mTitle AND year = mYear AND director = mDirector
    LIMIT 1;
    RETURN movie_id;
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION get_next_movie_id()
RETURNS VARCHAR(10)
READS SQL DATA
BEGIN
	DECLARE next_id INTEGER;
    SELECT get_next_id('movies') INTO next_id;
    RETURN CONCAT('tt', next_id);
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION get_next_star_id()
RETURNS VARCHAR(10)
READS SQL DATA
BEGIN
	DECLARE next_id INT;
    SELECT get_next_id('stars') INTO next_id;
    RETURN CONCAT('nm', next_id);
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION find_star_id(star_name VARCHAR(100), star_year INT)
RETURNS VARCHAR(10)
READS SQL DATA
BEGIN
	DECLARE star_id VARCHAR(10);
    IF star_year IS NULL THEN
		SELECT id
		INTO star_id
		FROM stars
		WHERE name = star_name AND birthYear IS NULL
		LIMIT 1;
	ELSE
		SELECT id
		INTO star_id
		FROM stars
		WHERE name = star_name AND birthYear = star_year
		LIMIT 1;
    END IF;
    RETURN star_id;    
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION find_star_id_by_name(star_name VARCHAR(100))
RETURNS VARCHAR(10)
READS SQL DATA
BEGIN
	DECLARE star_id VARCHAR(10);
    
	SELECT id
	INTO star_id
	FROM stars
	WHERE name = star_name
	LIMIT 1;
	
    RETURN star_id;    
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION find_movie_id_by_title(movie_title VARCHAR(100))
RETURNS VARCHAR(10)
READS SQL DATA
BEGIN
	DECLARE movie_id VARCHAR(10);
    
	SELECT id
	INTO movie_id
	FROM movies
	WHERE title = movie_title
	LIMIT 1;
	
    RETURN movie_id;    
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION get_next_id(`table_name` VARCHAR(32) ) 
RETURNS INTEGER
READS SQL DATA
BEGIN
	DECLARE last_id VARCHAR(10);
    
    IF `table_name` = 'movies' THEN
		SELECT MAX(id) INTO last_id FROM movies;
	ELSEIF `table_name` = 'stars' THEN
		SELECT MAX(id) INTO last_id FROM stars;
    END IF;
    
	RETURN CAST(SUBSTRING(last_id, 3) AS UNSIGNED) + 1;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE link_star_to_movie(
	IN star_id VARCHAR(10),
    IN movie_id VARCHAR(10))
BEGIN
	INSERT INTO stars_in_movies (starId, movieId)
		VALUES(star_id, movie_id);
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE add_genre(
	IN genre_name VARCHAR(32), 
    OUT genre_id INT,
    OUT message VARCHAR(100)	)
BEGIN
    SELECT find_genre_id(genre_name) INTO genre_id;
    IF (genre_id IS NULL) THEN
		INSERT INTO genres(name) VALUES(genre_name);
        SELECT MAX(id) INTO genre_id FROM genres;
        SET message = CONCAT(' New genre inserted. Genre ID: ', genre_id);
	ELSE 
		SET message = CONCAT(' Existing genre. Genre ID: ', genre_id);
	END IF;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE link_genre_to_movie(
	IN genre_id INTEGER,
    IN movie_id VARCHAR(10))
BEGIN
	INSERT INTO genres_in_movies (genreId, movieId)
		VALUES(genre_id, movie_id);
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION find_genre_id(genre_name VARCHAR(32))
RETURNS INTEGER
READS SQL DATA
BEGIN
	DECLARE genre_id INT;
    
    SELECT id
    INTO genre_id
    FROM genres
    WHERE name = genre_name;
    
    RETURN genre_id;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE link_star_name_to_movie_title(
	IN star_name VARCHAR(100),
    IN movie_title VARCHAR(100)	)
BEGIN
	DECLARE star_id VARCHAR(10);
    DECLARE movie_id VARCHAR(10);
    
    SET star_id = find_star_id_by_name(star_name);
    SET movie_id = find_movie_id_by_title(movie_title);
    
    IF star_id IS NOT NULL AND movie_id IS NOT NULL THEN
		INSERT INTO stars_in_movies (starId, movieId)
			VALUES(star_id, movie_id);
	END IF;
    
END $$
DELIMITER ;



