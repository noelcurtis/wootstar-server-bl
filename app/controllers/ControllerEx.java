package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import engine.Utils;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class ControllerEx extends Controller
{
    protected static Result gzippedOk(JsonNode node)
    {
        return gzippedOk(node, null);
    }


    protected static Result gzippedOk(JsonNode node, String etag)
    {
        try
        {
            ByteArrayOutputStream gzip = Utils.gzip(node.toString());
            response().setHeader(CONTENT_ENCODING, "gzip");
            response().setHeader(CONTENT_LENGTH, gzip.size() + "");
            if (!Strings.isNullOrEmpty(etag))
            {
                response().setHeader("ETag", etag);
            }
            response().setContentType("application/json");

            node = null; // Garbage Collect!!

            return ok(gzip.toByteArray());
        } catch (Exception ex)
        {
            Logger.error(ex.toString());
        }
        return ok(Utils.jsonError());
    }

    protected static Result byteArrayOk(byte[] result)
    {
        return byteArrayOk(result, null);
    }

    protected static Result byteArrayOk(byte[] result, String etag)
    {
        response().setHeader(CONTENT_ENCODING, "gzip");
        response().setHeader(CONTENT_LENGTH, result.length + "");
        if (!Strings.isNullOrEmpty(etag))
        {
            response().setHeader("ETag", etag);
        }
        response().setContentType("application/json");
        return ok(result);
    }

    protected static boolean eTagTest(String eTag)
    {
        if (!Strings.isNullOrEmpty(eTag))
        {
            Map<String, String[]> headers = request().headers();
            if (headers.containsKey("If-None-Match"))
            {
                String clientEtag = Objects.firstNonNull(headers.get("If-None-Match")[0], "");
                Logger.info("Client ETag " + clientEtag);
                if (eTag.equals(clientEtag))
                {
                    return true;
                }
            }
        }
        return false;
    }

    protected static Result noChange()
    {
        return status(304);
    }
}
