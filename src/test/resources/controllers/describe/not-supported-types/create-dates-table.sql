CREATE TABLE servicesql.timetest
(
  id integer NOT NULL,
  time time,
  description character varying(255),
  CONSTRAINT timetest_id_pk PRIMARY KEY (id)
);
