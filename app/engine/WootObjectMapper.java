package engine;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

public class WootObjectMapper
{
    private static ObjectMapper sharedInstance = null;

    public static ObjectMapper WootMapper()
    {
        if (sharedInstance == null)
        {
            sharedInstance = new ObjectMapper();
            sharedInstance.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        }
        return sharedInstance;
    }
}
