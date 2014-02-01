package engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import models.*;
import org.apache.commons.codec.binary.Base64;
import play.Logger;
import play.libs.Json;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;


public class Utils
{
    public static final int defaultSortOrder = 99999999;

    public static final int defaultWidth = 368;
    public static final int defaultHeight = 276;

    public static final Map<String, Integer> eventSortOrder;
    static
    {
        eventSortOrder = new HashMap<String, Integer>();
        eventSortOrder.put("www.woot.com", 1);
        eventSortOrder.put("wine.woot.com", 10);
        eventSortOrder.put("shirt.woot.com", 3);
        eventSortOrder.put("sellout.woot.com", 7);
        eventSortOrder.put("kids.woot.com", 9);
        eventSortOrder.put("home.woot.com",2);
        eventSortOrder.put("sport.woot.com", 4);
        eventSortOrder.put("tech.woot.com", 6);
        eventSortOrder.put("pop.woot.com", 11);
        eventSortOrder.put("tools.woot.com", 8);
        eventSortOrder.put("accessories.woot.com", 5);
    }

    public static JsonNode jsonNoChange()
    {
        Logger.info("Rendering No Change");
        ObjectNode result = Json.newObject();
        result.put("status", "ok");
        result.put("message", "no change");
        return result;
    }

    public static JsonNode jsonError(String message)
    {
        ObjectNode result = Json.newObject();
        result.put("status", "error");
        result.put("message", message);
        return result;
    }

    public static long toLong(String value, String defaultValue)
    {
        Optional<String> id = Optional.of(value);
        return Long.parseLong(id.or(defaultValue));
    }

    public static int toInt(String value, String defaultValue)
    {
        Optional<String> id = Optional.of(value);
        return Integer.parseInt(id.or(defaultValue));
    }

    public static double toDouble(String value, String defaultValue)
    {
        Optional<String> id = Optional.of(value);
        return Double.parseDouble(id.or(defaultValue));
    }

    public static ByteArrayOutputStream gzip(final String input)
            throws IOException
    {
        Logger.debug("Gzip content: start");
        final InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        final ByteArrayOutputStream stringOutputStream = new ByteArrayOutputStream((int) (input.length() * 0.75));
        final OutputStream gzipOutputStream = new GZIPOutputStream(stringOutputStream);

        final byte[] buf = new byte[5000];
        int len;
        while ((len = inputStream.read(buf)) > 0)
        {
            gzipOutputStream.write(buf, 0, len);
        }

        inputStream.close();
        gzipOutputStream.close();
        Logger.debug("Gzip content: end");
        return stringOutputStream;
    }

    public static void logMemoryUsage()
    {
        if (play.Play.application().configuration().getBoolean("logmemory.enabled"))
        {
            int mb = 1024 * 1024;
            //Getting the runtime reference from system
            Runtime runtime = Runtime.getRuntime();
            String message = "[Memory Usage] ";
            //Print used memory
            message += "Used Memory:"
                    + (runtime.totalMemory() - runtime.freeMemory()) / mb;

            //Print free memory
            message += " Free Memory:"
                    + runtime.freeMemory() / mb;

            //Print total available memory
            message += " Total Memory:" + runtime.totalMemory() / mb;

            //Print Maximum available memory
            message += " Max Memory:" + runtime.maxMemory() / mb;

            Logger.info(message);
        }
    }

    public static String getHostName()
    {
        try
        {
            String localhostname = java.net.InetAddress.getLocalHost().getHostName();
            return localhostname;
        } catch (Exception ex)
        {
            Logger.error("Error getting hostname: " + ex.toString());
        }
        return "";
    }

    public static String cleanStringOfHtmlTags(String value)
    {
        value = Strings.nullToEmpty(value);
        String cleaned = value.replaceAll("<p>|</p>", "").replaceAll("\r\n", " ").replaceAll("<em>|</em>", "").replaceAll("<div class=\"event-text\">|</div>", "");
        return cleaned;
    }

    public static String createETag(String value)
    {
        return "\"" + Hashing.sha256().hashString(value).toString() + "\"";
    }

    /**
     * Write the object to a Base64 string.
     */
    public static String serializeToString(Serializable o) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return new String(Base64.encodeBase64(baos.toByteArray()));
    }

    /**
     * Read the object from Base64 string.
     */
    public static Object deserializeFromString(String s) throws IOException, ClassNotFoundException
    {
        if (Strings.isNullOrEmpty(s)) return null;

        byte[] data = Base64.decodeBase64(s.getBytes());
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    public static String headersToString(Map<String, String[]> headers) throws Exception
    {
        return WootObjectMapper.WootMapper().writeValueAsString(headers);
    }

    public static int getEventSortOrder(String site)
    {
        if(eventSortOrder.containsKey(site))
        {
            return eventSortOrder.get(site);
        }
        return defaultSortOrder;
    }

    public static WpPhoto getDefaultImage(List<WpPhoto> photos)
    {
        for (WpPhoto p : photos)
        {
            if (Utils.toDouble(p.getWidth(), "0") == defaultWidth &&
                    Utils.toDouble(p.getHeight(), "0") == defaultHeight)
            {
                return p;
            }
        }
        return null;
    }
}
