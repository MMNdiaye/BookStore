ALTER TABLE publishers
ADD CONSTRAINT publisher_name_unique UNIQUE(name);

CREATE INDEX ON publishers(name);