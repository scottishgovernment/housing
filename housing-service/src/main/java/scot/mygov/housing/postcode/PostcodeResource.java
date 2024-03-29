package scot.mygov.housing.postcode;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.housing.forms.modeltenancy.validation.ValidationUtil;
import scot.mygov.validation.ValidationResultsBuilder;

import javax.inject.Inject;

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
            return Response
                    .status(400)
                    .entity(resultBuilder.build())
                    .build();
        }

        try {
            String normalisedPostcode = normalisePostcode(postcode);
            PostcodeServiceResults results = postcodeService.lookup(normalisedPostcode);
            return Response
                    .status(200)
                    .entity(results)
                    .build();

        } catch (PostcodeServiceException e) {
            LOG.error("Failed to get postcode {}", postcode, e);
            return Response
                    .status(503)
                    .entity("Postcode data not available")
                    .build();
        }
    }

    private String postcodeParam(MultivaluedMap<String, String> params) {
        if (params.containsKey(POSTCODE_PARAM)) {
            return params.getFirst(POSTCODE_PARAM).toUpperCase();
        } else {
            return null;
        }
    }

    private String normalisePostcode(String postcodeIn) {
        // make it upper case and remove all sapces
        String postcode = postcodeIn.toUpperCase().replace(" ", "");
        int threeFromEnd = postcode.length() - 3;
        // ensure it contains a space before the final three characters
        return String.format("%s %s", postcode.substring(0, threeFromEnd), postcode.substring(threeFromEnd));
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
