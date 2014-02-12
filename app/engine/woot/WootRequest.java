package engine.woot;

public class WootRequest
{
    public Long interval = 600000L; // interval in milliseconds if not set will make requests with 10min interval
    public WootApiHelpers.EventType eventType;
    public WootApiHelpers.Site site;

    public WootRequest(Long interval, WootApiHelpers.EventType eventType, WootApiHelpers.Site site)
    {
        this.interval = interval;
        this.eventType = eventType;
        this.site = site;
    }

    public WootRequest(WootApiHelpers.EventType eventType, WootApiHelpers.Site site)
    {
        this.eventType = eventType;
        this.site = site;
    }

    @Override
    public String toString()
    {
        return "interval " + interval + " eventType " + eventType + " site " + site;
    }

    public String getId()
    {
        StringBuilder sb = new StringBuilder();
        if (eventType != null)
        {
            sb.append(eventType.toString());
        }
        if(site != null)
        {
            sb.append("." + site.toString());
        }
        return sb.toString();
    }

}
