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

## Authentication
*   The server and application share a secret
*   You can find the secret in `application.config` under `application.secret`
*   When submitting an API request please include the following header `name=Authorization, value=Sha256(application.secret)`
*   You must encode application.secret at UTF-8, this should be default encoding for most hashing libraries
*   You might be able to use the following

```-(NSString*)sha256HashFor:(NSString*)input
        {
            const char* str = [input UTF8String];
            unsigned char result[CC_SHA256_DIGEST_LENGTH];
            CC_SHA256(str, strlen(str), result);

            NSMutableString *ret = [NSMutableString stringWithCapacity:CC_SHA256_DIGEST_LENGTH*2];
            for(int i = 0; i<CC_SHA256_DIGEST_LENGTH; i++)
            {
                [ret appendFormat:@"%02x",result[i]];
            }
            return ret;
        }```
*   Your output value of Sha256(application.secret) should be ab67d454e9def183dccf82040d213c8e5c63469d6ff90ec54ddee699ba298518 -- to be remove from doc shortly


## [Caching Design](https://github.com/noelcurtis/wootstar-server-bl/blob/master/docs/caching.md)

## [API Documentation] (https://github.com/noelcurtis/wootstar-server-bl/blob/master/docs/api.md)

## [Helpers] (https://github.com/noelcurtis/wootstar-server-bl/blob/master/docs/helpers.md)




