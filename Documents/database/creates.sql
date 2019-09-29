Wprowadzone w bazie:

create table app_user (
	id serial PRIMARY KEY,
	email varchar(40) NOT NULL,
	password varchar(20) NOT NULL,
  	authToken varchar(40),
    	expirationDate timestamp
);

create table travel (
	id serial PRIMARY KEY,
	name varchar(40) NOT NULL
);

create table app_user_travel (
	id serial PRIMARY KEY,
	app_user_id integer references app_user(id),
	travel_id integer references travel(id)
);

create table scan (
	id serial PRIMARY KEY,
	user_id integer references app_user(id),
	travel_id integer references travel(id),
	name varchar(40) NOT NULL
);

create table place (
	id serial PRIMARY KEY,
	title text,
	vicinity text,
	category text
);

create table plan_element (
	id serial PRIMARY KEY,
	locale text,
	from_date_time timestamp,
	to_date_time timestamp,
	place_id integer references place(id),
	travel_id integer references travel(id)
);

