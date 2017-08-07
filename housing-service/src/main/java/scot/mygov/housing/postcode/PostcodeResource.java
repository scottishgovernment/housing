package scot.mygov.housing.postcode;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.geosearch.api.models.Postcode;
import scot.mygov.housing.modeltenancy.validation.ValidationUtil;
import scot.mygov.housing.rpz.PostcodeSource;
import scot.mygov.validation.ValidationResults;
import scot.mygov.validation.ValidationResultsBuilder;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("postcode")
public class PostcodeResource {

    private static final Logger LOG = LoggerFactory.getLogger(PostcodeResource.class);

    private static final String POSTCODE_PARAM = "postcode";

    private final PostcodeService postcodeService;

    private final PostcodeSource postcodeSource;

    @Inject
    public PostcodeResource(PostcodeService postcodeService, PostcodeSource postcodeSource) {
        this.postcodeService = postcodeService;
        this.postcodeSource = postcodeSource;
    }

    @Path("address-lookup")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response lookup(@Context UriInfo uriInfo) {

        String postcode = postcodeParam(uriInfo.getQueryParameters());
        ValidationResults validationResult = validate(postcode);
        if (!validationResult.getIssues().isEmpty()) {
            return Response.status(400).entity(validationResult).build();
        }

        try {
            PostcodeServiceResults results = postcodeService.lookup(postcode);
            return Response
                    .status(200)
                    .entity(results)
                    .build();
        } catch (PostcodeServiceException e) {
            LOG.error("Failed to get postcode", e);
            return Response.status(503).entity("Postcode data not available").build();
        }
    }

    private String postcodeParam(MultivaluedMap<String, String> params) {
        if (params.containsKey(POSTCODE_PARAM)) {
            // the postcode param is case insensitive
            return params.getFirst(POSTCODE_PARAM).toUpperCase();
        } else {
            return null;
        }
    }
    private ValidationResults validate(String postcode) {
        ValidationResultsBuilder resultBuilder = new ValidationResultsBuilder();

        if (StringUtils.isEmpty(postcode)) {
            return resultBuilder.issue(POSTCODE_PARAM, "Missing required postcode param").build();
        }

        if (!ValidationUtil.validPostcode(postcode)) {
            return resultBuilder.issue(POSTCODE_PARAM, "Invalid postcode").build();
        }

        // is this a scottish postcode?
        Postcode postcodeObj = postcodeSource.postcode(postcode);
        if (postcodeObj == null) {
            return resultBuilder.issue(POSTCODE_PARAM, "Not a Scottish postcode").build();
        }
        return resultBuilder.build();
    }

}
