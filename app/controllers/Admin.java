package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import engine.Utils;
import engine.WootObjectMapper;
import engine.woot.WootApiHelpers;
import play.libs.F;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;

import static engine.metrics.Metrics.WootStarMetrics;

public class Admin extends Controller
{
    public static Result index()
    {
        return ok(views.html.admin.index.render(Utils.getHostName()));
    }

    public static Result metrics()
    {
        try
        {
            return ok(WootStarMetrics().getMapper().valueToTree(WootStarMetrics().getMetricsRegistry()));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return internalServerError(Utils.jsonError("check logs"));
    }

    public static F.Promise<Result> apiStatus()
    {
        final ObjectNode node = WootObjectMapper.WootMapper().createObjectNode();
        final F.Promise<Result> resultPromise = WS.url("http://wootstar-lb-1-510642144.us-east-1.elb.amazonaws.com/apiv1/events").get().flatMap(
                new F.Function<WS.Response, F.Promise<Result>>() {
                    public F.Promise<Result> apply(WS.Response response) {
                        node.put("event", true);
                        return WS.url("http://wootstar-lb-1-510642144.us-east-1.elb.amazonaws.com/apiv1/events").setQueryParameter("type", "Daily").get().map(
                                new F.Function<WS.Response, Result>() {
                                    public Result apply(WS.Response response) {
                                        node.put("type", true);
                                        return ok(node);
                                    }
                                }
                        );
                    }
                }
        );
        return resultPromise;
    }
}
