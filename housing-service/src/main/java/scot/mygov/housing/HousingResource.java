package scot.mygov.housing;

import scot.mygov.housing.rpz.RPZService;
import scot.mygov.housing.rpz.RPZResult;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;

@Path("/")
public class HousingResource {

    @Inject
    RPZService rpzService;

    @GET
    @Path("rpz")
    @Produces("application/json")
    public Response searchGet(@Context UriInfo uriInfo) {
        String postcode =  uriInfo.getQueryParameters().getFirst("postcode");
        String dateString =  uriInfo.getQueryParameters().getFirst("date");
        LocalDate date = LocalDate.parse(dateString);
        RPZResult result = rpzService.rpz(postcode, date);
        return Response.status(200).entity(result).build();
    }

}
