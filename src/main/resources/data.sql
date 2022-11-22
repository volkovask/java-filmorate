DELETE FROM FILM_LIKES;
DELETE FROM FILM_GENRE;
DELETE FROM USER_FRIENDS;
DELETE FROM USERS;
DELETE FROM FILMS;

ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE FILMS ALTER COLUMN ID RESTART WITH 1;

/*INSERT INTO FILMS (name, description, release_date, duration)
VALUES ( 'Nobody', 'New film', '2021-10-10', 120); */

MERGE INTO MPA KEY(ID) VALUES
    (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17');

MERGE INTO GENRE KEY(ID) VALUES
    (1, 'Боевик'),
    (2, 'Вестерн'),
    (3, 'Гангстерский фильм'),
    (4, 'Детектив'),
    (5, 'Драма'),
    (6, 'Исторический фильм'),
    (7, 'Комедия'),
    (8, 'Мелодрама'),
    (9, 'Музыкальный фильм'),
    (10, 'Нуар'),
    (11, 'Политический фильм'),
    (12, 'Приключенческий фильм'),
    (13, 'Сказка'),
    (14, 'Трагедия'),
    (15, 'Трагикомедия');