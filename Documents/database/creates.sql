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


insert into app_user (email,password) values ('jan.kowalski@gmail.com','password');
insert into travel (name) values ('Pary¿');
insert into travel (name) values ('Rzym');
insert into app_user_travel (app_user_id, travel_id) values (1,1);
insert into app_user_travel (app_user_id, travel_id) values (1,2);
