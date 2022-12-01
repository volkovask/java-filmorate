DELETE FROM FILM_LIKES;
DELETE FROM FILM_GENRE;
DELETE FROM USER_FRIENDS;
DELETE FROM USERS;
DELETE FROM FILMS;

ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE FILMS ALTER COLUMN ID RESTART WITH 1;

MERGE INTO MPA KEY(mpa_ID) VALUES
    (1, 'G'),
    (2, 'PG'),
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

INSERT INTO FILMS (name, release_date, description, duration, rate, mpa_id) VALUES ( 'New film_1', '1999-04-30', 'New film about friends', '120', '4', '3' );
INSERT INTO FILMS (name, release_date, description, duration, rate, mpa_id) VALUES ( 'New film_2', '2000-07-15', 'New film about holiday', '90', '4', '1' );

INSERT INTO FILM_GENRE (film_ID, genre_ID) VALUES (1, 2);
INSERT INTO FILM_GENRE (film_ID, genre_ID) VALUES (1, 4);
INSERT INTO FILM_GENRE (film_ID, genre_ID) VALUES (1, 6);
INSERT INTO FILM_GENRE (film_ID, genre_ID) VALUES (2, 5);

INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES ( 'user_1@yandex.ru', 'user_1', 'Fedor', '1990-05-05' );
INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES ( 'user_2@yandex.ru', 'user_2', 'Sonya', '1995-07-17' );
INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES ( 'user_3@yandex.ru', 'user_3', 'Peter', '2001-12-20' );

INSERT INTO USER_FRIENDS (USER_ID, FRIEND_ID) VALUES ( '1', '2' );
INSERT INTO USER_FRIENDS (USER_ID, FRIEND_ID) VALUES ( '1', '3' );
INSERT INTO USER_FRIENDS (USER_ID, FRIEND_ID) VALUES ( '2', '1' );
INSERT INTO USER_FRIENDS (USER_ID, FRIEND_ID) VALUES ( '3', '2' );

INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES ( '1', '1' );
INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES ( '1', '2' );