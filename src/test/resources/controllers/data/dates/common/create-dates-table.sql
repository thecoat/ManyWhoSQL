CREATE TABLE timetest
(
  id integer NOT NULL,
  time_with_timezone time,
  time_without_timezone time,
  timestamp_with_timezone timestamp,
  CONSTRAINT timetest_id_pk PRIMARY KEY (id)
);
