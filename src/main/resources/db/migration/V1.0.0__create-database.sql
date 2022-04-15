CREATE TABLE public.dna_history  (
	id        	bigserial NOT NULL,
	created_at	timestamp NOT NULL,
	dna       	text NOT NULL,
	dna_type  	varchar(20) NOT NULL,
	PRIMARY KEY(id)
);
