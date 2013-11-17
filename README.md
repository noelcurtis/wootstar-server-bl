# README	

## Getting Started	
*	Download Play 2.2.1, Postgres APP and Redis Server (brew install redis) before you do anything with the application
* 	Setup your database:
	*	Setup and Startup Postgres APP
	*	Run the following SQL commands to setup the database
	*	`$psql` will get you into the postgres console
	*	`CREATE USER play WITH PASSWORD 'play';` # create a user with password
	*	`CREATE DATABASE wootstar-dev;`
	*	`GRANT ALL PRIVILEGES ON DATABASE wootstar-dev to play;`
	*	Your Database is all setup for the application.
*	In the console run `$play dependencies` will get you started with managed dependencies
*	In the console run `$play idea` will setup the project to be opened with IntelliJ
*	You are all setup to start working with the project.
*	Note the first time you start the application play will ask you to run a DB Migration, please do so. The project will use DB Migrations to manage all DB changes so please do not run SQL to alter the DB manually.

## [Caching Design](https://github.com/noelcurtis/wootstar-server-bl/blob/master/docs/caching.md)




