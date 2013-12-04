package engine.actions;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import engine.Utils;
import play.Logger;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;

import java.util.Map;

public class SecuredAction extends Action<Secured>
{
    @Override
    public F.Promise<SimpleResult> call(Http.Context ctx) throws Throwable
    {
        Map<String, String[]> headers = ctx.request().headers();
        if (headers.containsKey("Authorization"))
        {
            final String auth = headers.get("Authorization")[0];
            String secret = play.Play.application().configuration().getString("application.secret");
            if (isSecure(auth, secret))
            {
                return delegate.call(ctx);
            }
        }

        Logger.error("Authentication Failure, someone might be trying to hack the API");
        Logger.info(ctx.request().toString());
        for(String k : headers.keySet())
        {
            Logger.info("Header " + k);
            for(String v : headers.get(k))
            {
                Logger.info("Value " + v);
            }
        }
        SimpleResult authError = ok(Utils.jsonError("authentication failure"));
        return F.Promise.pure(authError);
    }

    public static boolean isSecure(String authorization, String secret)
    {
        String[] authSplit = authorization.split(":");
        if (authSplit.length == 2)
        {
            String timestamp = authSplit[0];
            String compareHash = authSplit[1];
            String genHash = Hashing.sha256().newHasher().putString(timestamp + secret, Charsets.UTF_8).hash().toString();
            if (genHash.equals(compareHash))
            {
                return true;
            }
        }
        return false;
    }
}