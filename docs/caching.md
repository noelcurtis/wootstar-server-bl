## Caching Design

Daily: 15 minutes
WootPlus:


### Possible Woot Sites: 11 Sites in total

*   www.woot.com
*   wine.woot.com
*   shirt.woot.com
*   sellout.woot.com
*   kids.woot.com
*   home.woot.com
*   sport.woot.com
*   tech.woot.com
*   pop.woot.com
*   tools.woot.com
*   accessories.woot.com


### Possible Event Types: 5 Event types in total

*   Daily
*   Moofi
*   Reckoning
*   WootOff
*   WootPlus


## Methodolody
*   Total possible requests is 55 requests
*   Worker will process each request individually
*   Each request can have a separate refresh interval
        *   This means that a Wine Woot daily event can be refreshed more frequently than a Shirt Woot daily event et...
*   All requests will be started at the same time
*   No transaction management is required because each request will return different events so there should be no conflicts on DB write
*   Each request will have a separate cashing identifier -> _wine-daily-cache-checkpoint = NOV 10 2013 10:20:20_ etc etc
*   Each request will have a separate pg-lock identifer - > to allow for requests to be scaled across multiple workers (no needed right now)


## DB Updates - Worker
*   Worker starts up and makes request at respective request interval
*   Request and DB update for each request are asynchronous
*   After DB is updated for each request its respective checkpoint is updated

## Serving Responses - Application Node
*   For all Events
    *   Cache Test
        *   Go through each cache identifier to see if there is fresh data for any site - event combination
        *   If there is fresh data for a site - event combination pull data from the DB - Else pull (JSON) data from the cache
        *   Render all events as JSON
        *   Return

## Cached Object
*   Caching identifier _site-eventType-cache_ example:_wine-daily-cache_
*   Cached value: Object containing JSON as well as a list of Events -> this will allow JSON to be returned or raw events to be returned