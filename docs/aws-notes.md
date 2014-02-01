# AWS Notes

## General Server Configuration
*   Each server is setup from a *GOLD AMI* *wootstar-app-node*
*   The instance gets configured automatically after power on with an init.d script *play-init* in the project
*   The Script will
    *   Check out the master branch
    *   Setup config files
    *   clean compile stage
    *   start the app in the appropriate mode *worker* or *node* depending on the .conf file.


## Worker Configuration
*   *wootstar-app-worker* is the worker for the app
*   it is responsible for making requests to woot and updating the REDIS cache
*   like all the servers this server is configured with 2 status alarms
    *   wootstar-bl-worker-1-CPU-Utilization - High CPU Utilization
    *   wootstar-bl-worker-1-High-Status-Check-Failed-Any - Health status failed
*   An email will be sent out if any of these alarms go off  - you should be *scared*!


## Node Configuration
*   Nodes are configured in an *auto-scaling group*
*   The Group has a required level of 2 nodes, which means 2 nodes will always be up
*   The max scaling factor is 6 nodes, which means there can be upto 6 active nodes at any time
*   The scaling factor is based on the average CPU utilization of active nodes
*   > 80% scale up
*   < 10% scale down
*   Based on CPU utilization AWS should maintain the correct number of nodes
*   This way we can make sure that at peak hours we server traffic appropriately.


