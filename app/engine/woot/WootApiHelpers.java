package engine.woot;

public class WootApiHelpers
{
    public static final String wootKey = "041deb1d26d3411cbdb35abba0f6f709";
    public static final String wootUrl = "http://api.woot.com/2/events.json";

//    www.woot.com
//    wine.woot.com
//    shirt.woot.com
//    sellout.woot.com
//    kids.woot.com
//    home.woot.com
//    sport.woot.com
//    tech.woot.com
//    pop.woot.com
//    tools.woot.com
//    accessories.woot.com

    public enum Site
    {
        woot("www.woot.com"),
        wine("wine.woot.com"),
        shirt("shirt.woot.com"),
        sellout("sellout.woot.com"),
        kids("kids.woot.com"),
        home("home.woot.com"),
        sport("sport.woot.com"),
        tech("tech.woot.com"),
        pop("pop.woot.com"),
        tools("tools.woot.com"),
        accessories("accessories.woot.com");

        private final String value;
        private static final String identifier = "site";

        private Site(String site)
        {
            value = site;
        }

        public String getValue()
        {
            return value;
        }
    }

//    Daily
//    Moofi
//    Reckoning
//    WootOff
//    WootPlus

    public enum EventType
    {
        Daily,
        Moofi,
        Reckoning,
        WootOff,
        WootPlus;

        private static final String identifier = "eventType";

        public String getValue()
        {
            return this.toString(); // just to be consistent with the interface
        }

        public static EventType fromString(String type)
        {
            for (EventType e : EventType.values())
            {
                if (e.toString().toLowerCase().equals(type.toLowerCase()))
                {
                    return e;
                }
            }
            return Daily;
        }
    }

    public static String getCacheIdentifier(EventType eventType, Site site)
    {
        return getIdentifier(eventType, site, "-cache-identifier");
    }

    public static String getCheckpointIdentifier(EventType eventType, Site site)
    {
        return getIdentifier(eventType, site, "-checkpoint-identifier");
    }

    private static String getIdentifier(EventType eventType, Site site, String identifier)
    {
        if (eventType == null && site == null)
        {
            return "all-checkpoint-identifier";
        }
        String s = site != null ? site.toString() : "";
        String e = eventType != null ? eventType.toString() : "";
        return s + e + identifier;
    }
}
