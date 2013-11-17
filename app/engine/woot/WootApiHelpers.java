package engine.woot;

public class WootApiHelpers
{
    public static final String wootKey = "041deb1d26d3411cbdb35abba0f6f709";
    private static final String wootUrl = play.Play.application().configuration().getBoolean("testmode.enabled") ? "http://localhost:9000/test/": "http://api.woot.com/2/events.json";

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
    }
}
