# README wootstar-server-bl (bl: Blue Label the premium cut)

## Admin
Admin page is at: [http://wootstar-lb-1-510642144.us-east-1.elb.amazonaws.com/admin]

## Getting Started
*	Download Play 2.2.1
*   Postgres APP (__Only needed if you want to run the API__)
* 	Setup your database (__Only needed if you want to run the API__):
	*	Setup and Startup Postgres APP
	*	Run the following SQL commands to setup the database
	*	`psql` will get you into the postgres console
	*	`CREATE USER play WITH PASSWORD 'play';` create a user with password
	*	`CREATE DATABASE wootstar-dev;`
	*	`GRANT ALL PRIVILEGES ON DATABASE wootstar-dev to play;`
	*	Your Database is all setup for the application.
*	In the console run `play dependencies` will get you started with managed dependencies
*	In the console run `play idea` will setup the project to be opened with IntelliJ
*	You are all setup to start working with the project.
*   Copy `cp application.dev.con to application.conf`
*   Change `datagetter.enabled=false` to `datagetter.enabled=true` if you want to run the __data getter__.
*	Note the first time you start the application play will ask you to run a DB Migration, please do so. The project will use DB Migrations to manage all DB changes so please do not run SQL to alter the DB manually.
*   Open `http://localhost:9000` in your web browser you should see the home page in a few seconds.

## [Caching Design](https://github.com/noelcurtis/wootstar-server-bl/blob/master/docs/caching.md)

## [API Documentation] (https://github.com/noelcurtis/wootstar-server-bl/blob/master/docs/api.md)

## [Helpers] (https://github.com/noelcurtis/wootstar-server-bl/blob/master/docs/helpers.md)




