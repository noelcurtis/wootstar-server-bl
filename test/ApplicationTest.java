import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import engine.Utils;
import engine.actions.SecuredAction;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.junit.*;

import play.libs.WS;
import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {

    private class Results
    {
        public int succesfulRequests = 0;
        public int failedRequests = 0;
    }

    //@Test
    public void cleanHtmlTest()
    {
        String testHtml = "<p>HOLY ****!!! These shirts are so graphic, you can't take them anywhere! They're so graphic, Chris Rock blushes when he sees them! These tees are really graphic!!</p>";
        System.out.println(Utils.cleanStringOfHtmlTags(testHtml));
    }

    //@Test
    public void testSecured()
    {
        String hashed = Hashing.sha256().newHasher().putString("hello" + "qBPExhPs?C[?LZ]t2;rU8;vG[rpJF9dBBjNEwJU>@LpQ;Zs3c3DOQ]e9A^8bF;s<", Charsets.UTF_8).hash().toString();
        assertThat(SecuredAction.isSecure("hello:" + hashed, "qBPExhPs?C[?LZ]t2;rU8;vG[rpJF9dBBjNEwJU>@LpQ;Zs3c3DOQ]e9A^8bF;s<"));
    }

    //@Test
    public void loadTest() throws Exception
    {
        int interval = 500;
        int offset = 10;
        int limit = 10000;
        //String endpoint = "https://www.wootstar.com/apiv1/events";
        String endpoint = "http://localhost:9000/apiv1/events";
        final String auth = "hello:7805a2d65710e365ae645a8157bf4687d3922ee46146d1ea889b2ea8beec2188";
        final Results r = new Results();

        for (int i=0; i< limit; i++)
        {
                Thread.sleep(interval); // wait
                WS.WSRequestHolder wsHolder= WS.url(endpoint).setQueryParameter("id", "cc07cd84-93ff-4019-b2c4-583078fb0c70");
                final long time = System.currentTimeMillis();
                wsHolder.setHeader("Authorization", auth).get().map(new Function<WS.Response, Object>()
                {
                    @Override
                    public Object apply(WS.Response response) throws Throwable
                    {
                        if (response.getStatus() == Http.Status.OK)
                        {
                            r.succesfulRequests++;
                            System.out.println("Success " + r.succesfulRequests + " Failed " + r.failedRequests);
                        }
                        else
                        {
                            r.failedRequests++;
                            System.out.println("Failed: " + response.getStatus());
                        }
                        System.out.println("took: " + (System.currentTimeMillis()- time) +  "ms");
                        return null;
                    }
                });
        }

        System.out.println("Success " + r.succesfulRequests);
        System.out.println("Failed " + r.failedRequests);
    }


    @Test
    public void testTime()
    {
        final DateTime time = new DateTime();
        System.out.println(time);
        System.out.println(time.plusDays(1).toDateMidnight());
        org.joda.time.Duration duration = new org.joda.time.Duration(time, time.plusDays(1).toDateMidnight());
        System.out.println(duration.getStandardHours());
        // Add 5 hours for UTC + 1 hour for 1:00am
        duration = duration.plus(3600000*5);
        System.out.println(duration.getStandardSeconds());
    }
}
