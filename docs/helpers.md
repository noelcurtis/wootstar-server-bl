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

## Server Setup
*   sudo yum install java-devel
*   sudo yum install git
*   cd /opt
*   sudo wget http://downloads.typesafe.com/play/2.2.1/play-2.2.1.zip
*   sudo unzip play-2.2.1.zip
*   sudo chown -R ec2-user:ec2-user play-2.2.1
*   cd play-2.2.1
*   mkdir apps
*   cd apps
*   git clone https://github.com/noelcurtis/wootstar-server-bl.git
*   cd wootstar-server-bl
*   cp conf/application.aws.conf conf/application.conf
*   ../../play start

## Keystore information
Using keystore-file : /Users/noelcurtis/keystore.ImportKey
One certificate, no chain.
Key and certificate stored.
Alias:importkey  Password:importkey

## Servers
*   ssh -i EC2-KeyPair.pem ec2-user@ec2-54-204-52-123.compute-1.amazonaws.com
*   ssh -i EC2-KeyPair.pem ec2-user@ec2-54-197-43-144.compute-1.amazonaws.com


*   ssh -i EC2-KeyPair.pem ec2-user@ec2-54-226-138-137.compute-1.amazonaws.com

## Test Hash
*   hello:7805a2d65710e365ae645a8157bf4687d3922ee46146d1ea889b2ea8beec2188

##  AWS scheduled scaling configuration

# List group details
aws autoscaling describe-auto-scaling-groups --auto-scaling-group-names wootstar-bl-node-group

# Assign a scheduled scaling for your group
put-scheduled-update-group-action --auto-scaling-group-name wootstar-bl-node-group --scheduled-action-name Daily-ScaleUP --recurrence "30 5 * * *" --desired-capacity 5

# Describe your scheduled scaling
aws autoscaling describe-scheduled-actions --auto-scaling-group-name wootstar-bl-node-group

# See if your scheduled scaling happened
aws autoscaling describe-scaling-activities --auto-scaling-group-name wootstar-bl-node-group


