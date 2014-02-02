package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import engine.Utils;
import engine.WootObjectMapper;
import engine.actions.WithSsl;
import engine.metrics.ActiveUsersMonitor;
import play.libs.F;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;

import static engine.metrics.Metrics.WootStarMetrics;

public class Admin extends Controller
{
    @WithSsl
    public static Result index()
    {
        return ok(views.html.admin.index.render(Utils.getHostName()));
    }

    @WithSsl
    public static Result metrics()
    {
        try
        {
            final JsonNode node = WootStarMetrics().getMapper().valueToTree(WootStarMetrics().getMetricsRegistry());
            ((ObjectNode)node).put("activeUsers", ActiveUsersMonitor.getActiveUsers());
            ((ObjectNode)node).put("hostName", Utils.getHostName());
            return ok(node);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return internalServerError(Utils.jsonError("check logs"));
    }

    @WithSsl
    public static F.Promise<Result> apiStatus()
    {
        final String endpoint = play.Play.isDev() ? "http://localhost:9000/apiv1/events" : "https://wootstar.com/apiv1/events";
        final String auth = "hello:7805a2d65710e365ae645a8157bf4687d3922ee46146d1ea889b2ea8beec2188";
        final ObjectNode node = WootObjectMapper.WootMapper().createObjectNode();

        final F.Promise<Result> resultPromise = WS.url(endpoint).setHeader("Authorization", auth).get().flatMap(
                new F.Function<WS.Response, F.Promise<Result>>() {
                    public F.Promise<Result> apply(WS.Response response) {
                        node.put("event", true);
                        return WS.url(endpoint).setHeader("Authorization", auth).setQueryParameter("type", "Daily").get().map(
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
