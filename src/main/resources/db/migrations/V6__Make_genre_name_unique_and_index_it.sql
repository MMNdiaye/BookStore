ALTER TABLE genres
ADD CONSTRAINT unique_name UNIQUE(name);

CREATE INDEX ON genres(name);