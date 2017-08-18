package scot.mygov.housing.postcode;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.geosearch.api.models.Postcode;
import scot.mygov.housing.modeltenancy.validation.ValidationUtil;
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

    @Inject
    public PostcodeResource(PostcodeService postcodeService) {
        this.postcodeService = postcodeService;
    }

    @Path("address-lookup")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response lookup(@Context UriInfo uriInfo) {

        String postcode = postcodeParam(uriInfo.getQueryParameters());
        ValidationResultsBuilder resultBuilder = new ValidationResultsBuilder();
        validate(postcode, resultBuilder);

        if (resultBuilder.hasIssues()) {
            return Response.status(400).entity(resultBuilder.build()).build();
        }

        try {

            // normalise the postcode

            // look this postcode up using the postcode source.
//            Postcode postcodeObj = postcodeSource.postcode(postcode);
//            if (postcodeObj == null) {
//                resultBuilder.issue(POSTCODE_PARAM, "Not a Scottish postcode");
//                return Response.status(400).entity(resultBuilder.build()).build();
//            }

            String normalisedPostcode = normalisePostcode(postcode);
            PostcodeServiceResults results = postcodeService.lookup(normalisedPostcode);
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

    private String normalisePostcode(String postcodeIn) {
        // make it upper case and remove all sapces
        String postcode = postcodeIn.toUpperCase().replace(" ", "");
        if (postcode.charAt(3) == ' ') {
            return postcode.toUpperCase();
        } else {
            // ensure it contains a space
            int threeFromEnd = postcode.length() - 3;
            return String.format("%s %s", postcode.substring(0, threeFromEnd), postcode.substring(threeFromEnd));
        }
    }

    private void validate(String postcode, ValidationResultsBuilder resultBuilder) {
        if (StringUtils.isEmpty(postcode)) {
            resultBuilder.issue(POSTCODE_PARAM, "Missing required postcode param");
            return;
        }

        if (!ValidationUtil.validPostcode(postcode)) {
            resultBuilder.issue(POSTCODE_PARAM, "Invalid postcode");
            return;
        }
    }

}
