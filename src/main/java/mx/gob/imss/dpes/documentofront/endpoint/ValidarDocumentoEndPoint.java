package mx.gob.imss.dpes.documentofront.endpoint;

import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.documentofront.service.ValidateDocumentoService;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author antonio
 */
@Path("/validaExistencia")
@RequestScoped
public class ValidarDocumentoEndPoint extends BaseGUIEndPoint<BaseModel, BaseModel, BaseModel> {

  @Inject
  private ValidateDocumentoService validateDocumentoService;

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Valida si existe un documento",
      description = "Valida si existe un documento")
  public Response validateDocumento(Documento request) throws BusinessException {

    ServiceDefinition[] steps = {validateDocumentoService};
    Message<Documento> response = validateDocumentoService.executeSteps(steps, new Message<>(request));

    if (Message.isException(response) || response.getPayload().getRefDocumento() == null) {
      return Response.status(Response.Status.NO_CONTENT).build();
    }
    return Response.status(Response.Status.OK).build();
  }
}