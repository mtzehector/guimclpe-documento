/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.gob.imss.dpes.documentofront.endpoint;

import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.documentofront.assember.BovedaAssamber;
import mx.gob.imss.dpes.documentofront.model.DocumentoRequest;
import mx.gob.imss.dpes.documentofront.service.DocumentoBajaService;
import mx.gob.imss.dpes.documentofront.service.DocumentoCargaService;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 *
 * @author luisr.rodriguez
 */
@Path("/bajaDocumento")
@RequestScoped
public class BajaDocumentoEndPoint extends BaseGUIEndPoint<BaseModel, DocumentoRequest, BaseModel>{
    @Inject
    private DocumentoCargaService documentoCarga;//Steps 1
    @Inject
    private DocumentoBajaService bajaDocumento; //Steps 2
    @Inject
    private BovedaAssamber assembler;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Dar de baja un documento",
            description = "Dar de baja un documento si existe")
    public Response cancelarDocumento(DocumentoRequest request) throws BusinessException {
        //El request requiere la información del documento en el objeto documentResponse
        request.setDocumentoResponse(new Documento());
        request.getDocumentoResponse().setId(request.getDocumento().getId());
        request.getDocumentoResponse().setCveSolicitud(request.getDocumento().getCveSolicitud());
        request.getDocumentoResponse().setTipoDocumento(request.getDocumento().getTipoDocumento());
        log.log(Level.INFO, ">>>Documento a Consultar:{0}", request);
        //STEPS
        ServiceDefinition[] steps1 = {documentoCarga};
        Message<DocumentoRequest> resposeConsul = documentoCarga.executeSteps(steps1, new Message<>(request));
        if (Message.isException(resposeConsul)) {
            log.log(Level.INFO, ">>>Se da de baja el documento existente:{0}", resposeConsul);
            request.getDocumentoResponse().setRefDocumento(
                    resposeConsul.getPayload().getDocumentoResponse().getRefDocumento());
            ServiceDefinition[] steps2 = {bajaDocumento};
            bajaDocumento.executeSteps(steps2, new Message<>(resposeConsul.getPayload()));
        }
        return Response.ok(resposeConsul.getPayload().getDocumentoResponse()).build();
    }
    
}
