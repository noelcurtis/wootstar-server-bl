## Helpers

*   To start play in prod `play clean start`
*   Error events `/error.*application\s/i`


## Deployment/Production config
*   `cp application.aws.conf application.conf`
*   `cp logger-prod.xml logger.xml`
*   starting postgres `su postgres` `cd /opt` `pg_ctl -D pgdata start -l pglogs/pglog.log`
*   `../../play clean start` from application root
*   `psql -d "wootstar-dev" -U play` as user postgres if you want to connect to the DB
*   `rm /opt/wootstar/play-2.2.1/apps/wootstar-server-bl/RUNNING_PID` remove pid lock file




Header host
[info] application - Value wootstar-lb-1-510642144.us-east-1.elb.amazonaws.com
[info] application - Header X-Forwarded-For
[info] application - Value 54.242.54.176
[info] application - Header Connection
[info] application - Value keep-alive
[info] application - Header Accept
[info] application - Value */*
[info] application - Header X-Forwarded-Port
[info] application - Value 80
[info] application - Header X-Forwarded-Proto
[info] application - Value http
[info] application - Header User-Agent
[info] application - Value NING/1.0
[info] application - Responding with events count: 10
