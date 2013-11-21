## Helpers

*   To start play in prod `$play start`
*   Error events `/error.*application\s/i`


## Deployment/Production config
*   `cp application.aws.conf application.conf`
*   `cp logger-prod.xml logger.xml`
*   starting postgres `su postgres` `cd /opt` `pg_ctl -D pgdata start -l pglogs/pglog.log`
*   `../../play clean start` from application root
*   `psql -d "wootstar-dev" -U play` as user postgres if you want to connect to the DB
*   `rm /opt/wootstar/play-2.2.1/apps/wootstar-server-bl/RUNNING_PID` remove pid lock file
