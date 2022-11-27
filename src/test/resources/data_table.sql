DELETE FROM FILM_LIKES;
DELETE FROM FILM_GENRE;
DELETE FROM USER_FRIENDS;
DELETE FROM USERS;
DELETE FROM FILMS;

ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE FILMS ALTER COLUMN ID RESTART WITH 1;

MERGE INTO MPA KEY(mpa_ID) VALUES
    (2, 'PG'),
    (1, 'G'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17');

MERGE INTO GENRE KEY(genre_ID) VALUES
    (1, 'Комедия'),
    (2, 'Драма'),
    (3, 'Мультфильм'),
    (4, 'Триллер'),
    (5, 'Документальный'),
    (6, 'Боевик');

INSERT INTO FILMS (name, release_date, description, duration, rate, mpa_id) VALUES ('New film', '1999-04-30', 'New film about friends', '120', '4', '3');