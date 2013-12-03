import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
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

//    @Test
//    public void simpleCheck() {
//        int a = 1 + 1;
//        assertThat(a).isEqualTo(2);
//    }
//
//    @Test
//    public void renderTemplate() {
//        Content html = views.html.index.render("Your new application is ready.");
//        assertThat(contentType(html)).isEqualTo("text/html");
//        assertThat(contentAsString(html)).contains("Your new application is ready.");
//    }

    private class Results
    {
        public int succesfulRequests = 0;
        public int failedRequests = 0;
    }

    @Test
    public void loadTest() throws Exception
    {
        int interval = 100;
        int offset = 10;
        int limit = 10000;
        String endpoint = "http://wootstar-lb-1-510642144.us-east-1.elb.amazonaws.com/apiv1/events";
        final Results r = new Results();

        for (int i=0; i< limit; i++)
        {
                Thread.sleep(interval); // wait
                WS.WSRequestHolder wsHolder= WS.url(endpoint);
                final long time = System.currentTimeMillis();
                wsHolder.get().map(new Function<WS.Response, Object>()
                {
                    @Override
                    public Object apply(WS.Response response) throws Throwable
                    {
                        if (response.getStatus() == Http.Status.OK)
                        {
                            System.out.println("Success " + r.succesfulRequests + " Failed " + r.failedRequests);
                            r.succesfulRequests++;
                        }
                        else
                        {
                            System.out.println("Failed: " + response.getStatus());
                            r.failedRequests++;
                        }
                        System.out.println("took: " + (System.currentTimeMillis()- time) +  "ms");
                        return null;
                    }
                });
        }

        System.out.println("Success " + r.succesfulRequests);
        System.out.println("Failed " + r.failedRequests);
    }

}
