ALTER TABLE editions
RENAME TO publishers;

ALTER TABLE books
RENAME COLUMN edition_id TO publisher_id