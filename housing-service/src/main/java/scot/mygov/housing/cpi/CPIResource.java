package scot.mygov.housing.cpi;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scot.mygov.validation.ValidationResults;
import scot.mygov.validation.ValidationResultsBuilder;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Path("cpi")
public class CPIResource {

    private static final Logger LOG = LoggerFactory.getLogger(CPIResource.class);

    CPIService cpiService;

    @Inject
    public CPIResource(CPIService cpiService) {
        this.cpiService = cpiService;
    }

    private enum CPI_PARAM {
        FROM_DATE("from_date"), TO_DATE("to_date");

        private final String param;

        CPI_PARAM(String param) {
            this.param = param;
        }

        public String getParam() {
            return param;
        }
    }

    @GET
    @Path("cpi-delta")
    @Produces("application/json")
    public Response cpiDelta(@Context UriInfo uriInfo) throws CPIServiceException {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();
        ValidationResults validationResults = validateCPIParams(params);
        if (!validationResults.getIssues().isEmpty()) {
            return Response.status(400).entity(validationResults).build();
        }

        try {
            LocalDate fromDate = LocalDate.parse(params.getFirst(CPI_PARAM.FROM_DATE.getParam()));
            LocalDate toDate = LocalDate.parse(params.getFirst(CPI_PARAM.TO_DATE.getParam()));
            double cpiDelta = cpiService.cpiDelta(fromDate, toDate);
            return Response.status(200).entity(Double.valueOf(cpiDelta)).build();
        } catch (CPIServiceException e) {
            LOG.error("Failed to get cpiDelta", e);
            return Response.status(503).entity("CPI data not available").build();
        }
    }

    private ValidationResults validateCPIParams(MultivaluedMap<String, String> params) {

        ValidationResultsBuilder resultBuilder = new ValidationResultsBuilder();
        for (CPI_PARAM param : CPI_PARAM.values()) {
            validateDateParam(param.getParam(), params.getFirst(param.getParam()), resultBuilder);
        }

        if (!resultBuilder.hasIssues()) {
            LocalDate from = LocalDate.parse(params.getFirst(CPI_PARAM.FROM_DATE.getParam()));
            LocalDate to = LocalDate.parse(params.getFirst(CPI_PARAM.TO_DATE.getParam()));
            if (to.isBefore(from)) {
                resultBuilder.issue("dates", "to_date cannot be before from_date");
            }
        }
        return resultBuilder.build();
    }

    private void validateDateParam(String param, String value, ValidationResultsBuilder resultBuilder) {
        if (value == null) {
            resultBuilder.issue(param, "Missing required param");
            return;
        }

        try {
            LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            LOG.warn("Invalid date for param:" + param, e);
            resultBuilder.issue(param, "Invalid date:" + value);
        }
    }
}
