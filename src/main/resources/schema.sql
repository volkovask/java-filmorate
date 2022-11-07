CREATE TABLE IF NOT EXISTS film (
                       film_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                       name varchar(40),
                       description varchar(255),
                       release_date date,
                       duration integer
);