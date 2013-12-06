package engine.actions;

import com.google.common.base.Strings;
import engine.Utils;
import play.Logger;
import play.Play;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;

import java.util.Map;

public class WithSslAction extends Action<WithSsl>
{
    @Override
    public F.Promise<SimpleResult> call(Http.Context ctx) throws Throwable
    {
        // SSL redirect only if property is set
        if (!Boolean.parseBoolean(Strings.nullToEmpty(Play.application().configuration().getString("ssl.redirect"))))
        {
            return delegate.call(ctx);
        }

        Map<String, String[]> headers = ctx.request().headers();
        if (headers.get("X-Forwarded-Proto") != null)
        {
            final String protocol = headers.get("X-Forwarded-Proto")[0];
            if (protocol.toLowerCase().equals("https"))
            {
                return delegate.call(ctx);
            }
        }

        // redirect to https endpoint
        final StringBuilder sb = new StringBuilder();
        sb.append("https://");
        sb.append(ctx.request().host());
        sb.append(ctx.request().uri());
        Logger.info("Redirecting for https \n" + Utils.headersToString(ctx.request().headers()));
        return F.Promise.pure(redirect(sb.toString()));
    }
}
