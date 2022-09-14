DROP TABLE IF EXISTS public.validation;

CREATE TABLE public.validation
(
    id          SERIAL NOT NULL,
    studentUuid character varying,
    isValid     boolean,
    PRIMARY KEY (id)
);