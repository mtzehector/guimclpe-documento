package mx.gob.imss.dpes.documentofront.restclient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author luisr.rodriguez
 */
@RegisterRestClient
@Path("/solicitud")
public interface SolicitudBackClient {
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSolicitud(@PathParam("id") Long id);
    
}
