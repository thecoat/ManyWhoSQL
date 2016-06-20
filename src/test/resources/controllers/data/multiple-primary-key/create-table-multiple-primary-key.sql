CREATE TABLE IF NOt EXISTS public.city
(
  cityname character varying(255),
  countryname character varying(255),
  description character varying(1024),
  CONSTRAINT city_pk PRIMARY KEY (cityname, countryname)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.city
  OWNER TO postgres;