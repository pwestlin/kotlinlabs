CREATE TABLE movies
(
  id    INTEGER IDENTITY PRIMARY KEY,
  title varchar(40) unique NOT NULL
);