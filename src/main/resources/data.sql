DELETE FROM FILMS;
ALTER TABLE FILMS ALTER COLUMN ID RESTART WITH 1;

INSERT INTO FILMS (name, description, release_date, duration)
VALUES ( 'Nobody', 'New film', '2021-10-10', 120);