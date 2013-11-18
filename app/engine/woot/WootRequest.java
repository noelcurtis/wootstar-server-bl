package engine.woot;

public class WootRequest
{
    //public Long interval = 600000L; // interval in milliseconds if not set will make requests with 0 interval
    public Long interval = 40000l;
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
        return " interval " + interval + " eventType " + eventType + " site " + site;
    }

}
