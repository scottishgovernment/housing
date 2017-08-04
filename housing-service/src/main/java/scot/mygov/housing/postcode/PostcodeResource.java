package scot.mygov.housing.postcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Inject
    public PostcodeResource(PostcodeService postcodeService) {
        this.postcodeService = postcodeService;
    }

    @Path("address-lookup")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response lookup(@Context UriInfo uriInfo) {

        ValidationResults validationResult = validateParams(uriInfo.getQueryParameters());
        if (!validationResult.getIssues().isEmpty()) {
            return Response.status(400).entity(validationResult).build();
        }

        String postcode =  uriInfo.getQueryParameters().getFirst(POSTCODE_PARAM);
        try {
            PostcodeServiceResults results = postcodeService.lookup(postcode);
            return Response.status(200).entity(results).build();
        } catch (PostcodeServiceException e) {
            LOG.error("Failed to get postcode", e);
            return Response.status(503).entity("Postcode data not available").build();
        }
    }

    private ValidationResults validateParams(MultivaluedMap<String, String> params) {
        ValidationResultsBuilder resultBuilder = new ValidationResultsBuilder();
        if (!params.containsKey(POSTCODE_PARAM)) {
            resultBuilder.issue(POSTCODE_PARAM, "Missing required postcode param");
        }
        return resultBuilder.build();
    }

}
