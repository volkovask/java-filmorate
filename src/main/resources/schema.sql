CREATE TABLE IF NOT EXISTS MPA
(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(100)
);
CREATE TABLE IF NOT EXISTS FILMS
(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(100),
    release_date date,
    description varchar(200),
    duration integer,
    rate integer,
    mpa_ID BIGINT NOT NULL REFERENCES MPA
);
CREATE TABLE IF NOT EXISTS USERS
(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar(100),
    login varchar(100),
    name varchar(100),
    birthday date
);
CREATE TABLE IF NOT EXISTS USER_FRIENDS
(
    user_ID BIGINT NOT NULL REFERENCES USERS,
    friend_ID BIGINT NOT NULL REFERENCES USERS,
    PRIMARY KEY (user_ID, friend_ID)
);
CREATE TABLE IF NOT EXISTS FILM_LIKES
(
    film_ID BIGINT NOT NULL REFERENCES FILMS,
    user_ID BIGINT NOT NULL REFERENCES USERS
);
CREATE TABLE IF NOT EXISTS GENRE
(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(100)
);
CREATE TABLE IF NOT EXISTS FILM_GENRE
(
    film_ID BIGINT NOT NULL REFERENCES FILMS,
    genre_ID BIGINT NOT NULL REFERENCES GENRE
);