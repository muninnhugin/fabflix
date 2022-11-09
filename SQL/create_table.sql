CREATE TABLE movies (
	id 			varchar(10),
    title 		varchar(100) NOT NULL,
    `year`		int NOT NULL,
    director	varchar(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE ratings (
	movieId		varchar(10),
    rating		float NOT NULL,
    numVotes	int NOT NULL,
    PRIMARY KEY	(movieId),
    FOREIGN KEY (movieId) REFERENCES movies(id)
);

CREATE TABLE stars (
	id			varchar(10),
    `name`		varchar(100),
    birthYear	int,
    PRIMARY KEY (id)
);

CREATE TABLE stars_in_movies (
	starId		varchar(10) NOT NULL,
    movieId		varchar(10) NOT NULL,
    FOREIGN KEY (starId) REFERENCES stars(id),
    FOREIGN KEY (movieId) REFERENCES movies(id)
);

CREATE TABLE genres (
	id			int AUTO_INCREMENT,
    `name`		varchar(32) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE genres_in_movies (
	genreId			int NOT NULL,
	movieId			varchar(10) NOT NULL,
    FOREIGN KEY (genreId) REFERENCES genres(id),
    FOREIGN KEY (movieId) REFERENCES movies(id)
);

CREATE TABLE customers (
	id				int AUTO_INCREMENT,
    firstName		varchar(50) NOT NULL,
    lastName		varchar(50) NOT NULL,
    ccId			varchar(20) NOT NULL,
    address			varchar(200) NOT NULL,
    email			varchar(50) NOT NULL,
    `password`		varchar(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE sales (
	id				int AUTO_INCREMENT,
    customerId		int NOT NULL,
	movieId			varchar(10) NOT NULL,
    saleDate		date NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE creditcards (
	id				varchar(20),
    firstName		varchar(50) NOT NULL,
    lastName		varchar(50) NOT NULL,
    expiration		date NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE employees (
	email 		varchar(50),
	password 	varchar(128) NOT NULL,
	fullname 	varchar(100),
	PRIMARY KEY (email)
);
