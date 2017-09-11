package scot.mygov.housing.rpz;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import scot.mygov.housing.mapcloud.Mapcloud;
import scot.mygov.housing.mapcloud.MapcloudException;
import scot.mygov.housing.mapcloud.MapcloudResult;
import scot.mygov.housing.mapcloud.MapcloudResults;
import scot.mygov.housing.rpz.model.ESTemplateQuery;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by z441571 on 30/08/2017.
 */
public class ElasticSearchRPZService implements RPZService {

    private final Mapcloud mapcloud;
    private final WebTarget target;

    public ElasticSearchRPZService(Mapcloud mapcloud, WebTarget target) {
        this.mapcloud = mapcloud;
        this.target = target;
    }

    @Override
    public RPZResult rpz(String uprn, LocalDate date) throws RPZServiceException {
        String postcode =  postcodeForUprn(uprn);
        ESTemplateQuery query = buildTemplateQuery(uprn, postcode, date);
        ObjectNode result = performQuery(query);

        JsonNode hits = result.get("hits");
        int hitCount = hits.get("total").asInt();
        if (hitCount == 0) {
            return new RPZResult.Builder().inRentPressureZone(false).build();
        }

        JsonNode source = hits.get("hits").get(0).get("_source");
        String name = source.get("name").asText();
        double maxIncrease = source.get("maxIncrease").asDouble();

        return new RPZResult.Builder()
                .inRentPressureZone(true)
                .title(name)
                .maxIncrease(maxIncrease).build();
    }

    private ObjectNode performQuery(ESTemplateQuery query) throws RPZServiceException {
        try{
            return target.request()
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.json(query)).readEntity(ObjectNode.class);
        } catch (ProcessingException | WebApplicationException ex) {
            throw new RPZServiceException("Failed to execute query", ex);
        }
    }
    private ESTemplateQuery buildTemplateQuery(String uprn, String postcode, LocalDate date) throws RPZServiceException {
        Map<String, String> params = new HashMap<>();
        params.put("uprn", uprn);
        params.put("postcode", postcode);
        params.put("date", date.toString());
        return new ESTemplateQuery(loadQuery(), params);
    }

    private String loadQuery() throws RPZServiceException {
        try {
            InputStream in = ElasticSearchRPZService.class.getResourceAsStream("/esQuery.json");
            return IOUtils.toString(in, "UTF-8");
        } catch (IOException e) {
            throw new RPZServiceException("Failed to load query template", e);
        }
    }

    private String postcodeForUprn(String uprn) throws RPZServiceException {
        try {
            MapcloudResults results = mapcloud.lookupUprn(uprn);
            if (results.getResults().size() != 1) {
                throw new RPZServiceException("Expected one postcode for uprn " + uprn);
            }

            MapcloudResult result = results.getResults().get(0);
            return result.getPostcode();
        } catch (MapcloudException e) {
            if ("Failed to lookup postcode".equals(e.getMessage())) {
                throw new RPZServiceClientException(Collections.singletonMap("uprn", "Invalid UPRN"));
            } else {
                throw new RPZServiceException("Failed to fetch postcode for UPRN: " + uprn, e);
            }
        }
    }

}
