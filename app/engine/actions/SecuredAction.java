package engine.actions;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import engine.Utils;
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
        String secret = play.Play.application().configuration().getString("application.secret");
        String genHash = Hashing.sha256().newHasher().putString(secret , Charsets.UTF_8).hash().toString();
        Map<String, String[]> headers = ctx.request().headers();
        if (headers.containsKey("Authorization"))
        {
            if (genHash.equals(headers.get("Authorization")[0]))
            {
                return delegate.call(ctx);
            }
        }

        SimpleResult authError = ok(Utils.jsonError("authentication failure"));
        return F.Promise.pure(authError);
    }
}