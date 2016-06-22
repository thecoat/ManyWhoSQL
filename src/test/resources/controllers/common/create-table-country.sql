CREATE TABLE servicesql.country(
  id integer NOT NULL,
  name character varying(255),
  description character varying(1024),
  CONSTRAINT country_id_pk PRIMARY KEY (id)
);