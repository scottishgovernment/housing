package scot.mygov.housing.rpz;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import scot.mygov.housing.europa.Europa;
import scot.mygov.housing.europa.EuropaAddress;
import scot.mygov.housing.europa.EuropaException;
import scot.mygov.housing.europa.EuropaResults;
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

import static java.util.Objects.isNull;


/**
 * Created by z441571 on 30/08/2017.
 */
public class ElasticSearchRPZService implements RPZService {

    private final Europa europa;
    private final WebTarget target;

    public ElasticSearchRPZService(Europa europa, WebTarget target) {
        this.europa = europa;
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
            return new RPZResult.Builder()
                    .inRentPressureZone(false)
                    .build();
        }

        JsonNode source = hits.get("hits").get(0).get("_source");
        String name = source.get("name").asText();
        String fromDate = source.get("fromDate").asText();
        String toDate = source.get("toDate").asText();
        double maxIncrease = source.get("maxIncrease").asDouble();

        return new RPZResult.Builder()
                .inRentPressureZone(true)
                .title(name)
                .dateFrom(fromDate)
                .dateTo(toDate)
                .maxIncrease(maxIncrease).build();
    }

    private ObjectNode performQuery(ESTemplateQuery query) throws RPZServiceException {
        try{
            ObjectNode result = target
                    .request()
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.json(query))
                    .readEntity(ObjectNode.class);

            if (isNull(result)) {
                throw new RPZServiceException("Null result from Elasticsearch");
            }

            if (!result.has("hits")) {
                throw new RPZServiceException("Invalid result from Elasticsearch (no hits). JSON: " + result.toString());
            }

            return result;
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
            EuropaResults results = europa.lookupUprn(uprn);
            if (!results.hasResults()) {
                throw new RPZServiceException("Expected one postcode for uprn " + uprn);
            }

            EuropaAddress result = results.getResults().get(0).getAddress().get(0);
            return result.getPostcode();
        } catch (EuropaException e) {
            if ("Failed to lookup uprn".equals(e.getMessage())) {
                throw new RPZServiceClientException(Collections.singletonMap("uprn", "Invalid UPRN"));
            } else {
                throw new RPZServiceException("Failed to fetch postcode for UPRN: " + uprn, e);
            }
        }
    }

}
