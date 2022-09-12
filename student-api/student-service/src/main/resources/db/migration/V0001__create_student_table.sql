DROP TABLE IF EXISTS public.student;

CREATE TABLE public.student
(
    id   SERIAL NOT NULL,
    data jsonb  NOT NULL,
    uuid character varying,
    PRIMARY KEY (id)
);