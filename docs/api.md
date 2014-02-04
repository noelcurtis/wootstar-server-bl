#API Documentation

## Summary
This API is built for Wootstar App.

## GET Events

API objects directly mimic the [Woot API](http://api.woot.com/2/docs/events). Please reference the Woot API for additional details.

### Sample Requests

* For __*all*__ events: __endpoint__ apiv1/events: This will return all events, WootPlus events will be returned __without any offers__.
* For __*WootOff*__ events: __endpoint__/events?type=WootOff: This will return all events of a specific type, offers will be included as well.
* For event by __*id*__ events: __endpoint__/events?id=6364ab63-339d-4b04-a8eb-30ed761b88ce: This will return details of an event by id.

*   url: /apiv1/settings
*   method: GET
*   headers
        *   Authorization: timestamp:secret
        *   Content-Type: application/json

__endpoint:__ http://wootstar.herokuapp.com

__example with endpoint:__

* http://ec2-23-22-201-81.compute-1.amazonaws.com:9000/apiv1/events
* http://ec2-23-22-201-81.compute-1.amazonaws.com:9000/apiv1/events?type=Daily
* http://ec2-23-22-201-81.compute-1.amazonaws.com:9000/apiv1/events?type=WootPlus
* http://ec2-23-22-201-81.compute-1.amazonaws.com:9000/apiv1/events?id=6364ab63-339d-4b04-a8eb-30ed761b88ce

## Using ETags
Pass If-None-Match with returned ETag

## Settings Endpoint

### GET Settings:
*   url: /apiv1/settings
*   method: GET
*   headers
        *   Authorization: timestamp:secret
        *   Content-Type: application/json

### Update Settings
*   url: apiv1/settings
*   method: POST
*   headers
    *   Authorization: timestamp:secret
    *   Content-Type: application/json
*   body: Any RAW JSON you want to update the settings to


## Metrics API

### Start Session
*   url: /metrics/startsession
*   method: POST
*   headers
    *   Authorization: timestamp:secret
    *   Content-Type: application/json
*   body: Nothing


### End Session
*   url: /metrics/endsession
*   method: POST
*   headers
    *   Authorization: timestamp:secret
    *   Content-Type: application/json
*   body: Nothing