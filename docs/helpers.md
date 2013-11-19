## Helpers

*   To start play in prod `$play start`
*   Error events `/error.*application\s/i`


## Deployment/Production config
*   `cp application.aws.conf application.conf`
*   `cp logger-prod.xml logger.xml`
*   starting postgres `su postgres` `cd /opt` `pg_ctl -D pgdata start -l pglogs/pglog.log`
*   `../../play start` from application root
