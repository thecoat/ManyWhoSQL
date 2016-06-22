CREATE TABLE servicesql.timetest
(
  id integer NOT NULL,
  time_with_timezone time with time zone,
  time_without_timezone time without time zone,
  timestamp_with_timezone timestamp with time zone,
  timestamp_without_timezone timestamp without time zone,
  CONSTRAINT timetest_id_pk PRIMARY KEY (id)
);
