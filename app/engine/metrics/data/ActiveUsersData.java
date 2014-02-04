package engine.metrics.data;

import com.fasterxml.jackson.databind.JsonNode;
import engine.WootObjectMapper;

/**
 * Data for active users
 */
public class ActiveUsersData
{
    public int count;
    public int averageActiveMillis;

    public ActiveUsersData(int count, int averageActiveMillis)
    {
        this.count = count;
        this.averageActiveMillis = averageActiveMillis;
    }

    public JsonNode toJson()
    {
        return WootObjectMapper.WootMapper().valueToTree(this);
    }
}
