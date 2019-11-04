Wprowadzone w bazie:

create table app_user (
	id serial PRIMARY KEY,
	email varchar(40) NOT NULL,
	password varchar(20) NOT NULL
);

create table travel (
	id serial PRIMARY KEY,
	name varchar(40) NOT NULL,
	image_url varchar(40)
);

create table app_user_travel (
	id serial PRIMARY KEY,
	app_user_id integer references app_user(id),
	travel_id integer references travel(id)
);

create table scan (
	id serial PRIMARY KEY,
	app_user_id integer references app_user(id),
	travel_id integer references travel(id),
	name varchar(40) NOT NULL
);

create table place (
	id serial PRIMARY KEY,
	here_id text,
	href text,
	title text,
	vicinity text,
	category integer,
	average_rating real,
	rates_count integer
);

create table plan (
	id serial PRIMARY KEY,
	from_date_time timestamp,
	place_id integer references place(id),
	travel_id integer references travel(id),
	completed boolean
);

create table app_user_friend (
	id serial PRIMARY KEY,
	app_user_id integer references app_user(id),
	friend_user_id integer references app_user(id)
);

create table app_user_place (
	id serial PRIMARY KEY,
	app_user_id integer references app_user(id),
	place_id integer references place(id),
	rating integer
);
