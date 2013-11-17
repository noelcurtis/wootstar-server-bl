package engine.data.apiv1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import engine.WootObjectMapper;

public class Settings
{
    private Settings()
    {

    }

    public static JsonNode getSettings()
    {
        ObjectMapper mapper = WootObjectMapper.WootMapper();
        ArrayNode siteDetails = mapper.createArrayNode();

        ObjectNode node = mapper.createObjectNode();
        node.put("site", "www.woot.com");
        node.put("color", "669510");
        node.put("cell_title", "woot!");
        node.put("woot_plus_title", "woot+");
        siteDetails.add(node);

        node = mapper.createObjectNode();
        node.put("type", "Moofi");
        node.put("site", "www.woot.com");
        node.put("color", "669510");
        node.put("cell_title", "moofi!");
        node.put("woot_plus_title", "moofi+");
        siteDetails.add(node);

        node = mapper.createObjectNode();
        node.put("site", "tech.woot.com");
        node.put("color", "108487");
        node.put("cell_title", "tech!");
        node.put("woot_plus_title", "tech+");
        siteDetails.add(node);

        node = mapper.createObjectNode();
        node.put("site", "home.woot.com");
        node.put("color", "d35500");
        node.put("cell_title", "home!");
        node.put("woot_plus_title", "home+");
        siteDetails.add(node);

        node = mapper.createObjectNode();
        node.put("site", "sport.woot.com");
        node.put("color", "6abb01");
        node.put("cell_title", "sport!");
        node.put("woot_plus_title", "sport+");
        siteDetails.add(node);

        node = mapper.createObjectNode();
        node.put("site", "kids.woot.com");
        node.put("color", "ffb510");
        node.put("cell_title", "kids!");
        node.put("woot_plus_title", "kids+");
        siteDetails.add(node);

        node = mapper.createObjectNode();
        node.put("site", "shirt.woot.com");
        node.put("color", "0071b0");
        node.put("cell_title", "shirt!");
        node.put("woot_plus_title", "shirt+");
        siteDetails.add(node);

        node = mapper.createObjectNode();
        node.put("site", "wine.woot.com");
        node.put("color", "891b28");
        node.put("cell_title", "wine!");
        node.put("woot_plus_title", "wine+");
        siteDetails.add(node);

        node = mapper.createObjectNode();
        node.put("site", "sellout.woot.com");
        node.put("color", "467c32");
        node.put("cell_title", "sellout!");
        node.put("woot_plus_title", "sellout+");
        siteDetails.add(node);

        node = mapper.createObjectNode();
        node.put("site", "tools.woot.com");
        node.put("color", "ff8500");
        node.put("cell_title", "tools & garden!");
        node.put("woot_plus_title", "tools & garden+");
        siteDetails.add(node);

        node = mapper.createObjectNode();
        node.put("site", "accessories.woot.com");
        node.put("color", "6230aa");
        node.put("cell_title", "accessories & watches!");
        node.put("woot_plus_title", "accessories & watches+");
        siteDetails.add(node);

        ObjectNode results = mapper.createObjectNode();
        results.put("site_details", siteDetails);

        ObjectNode resultsWrapper = mapper.createObjectNode();
        resultsWrapper.put("settings", results);

        return resultsWrapper;
    }
}
