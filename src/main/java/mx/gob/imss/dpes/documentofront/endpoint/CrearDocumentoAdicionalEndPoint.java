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
import mx.gob.imss.dpes.documentofront.model.MultipartBody;
import mx.gob.imss.dpes.documentofront.model.SolicitudRequest;
import mx.gob.imss.dpes.documentofront.service.BovedaService;
import mx.gob.imss.dpes.documentofront.service.DocumentoBajaService;
import mx.gob.imss.dpes.documentofront.service.DocumentoConsultaService;
import mx.gob.imss.dpes.documentofront.service.DocumentoService;
import mx.gob.imss.dpes.documentofront.service.SolicitudBackService;
import mx.gob.imss.dpes.interfaces.solicitud.model.EstadoSolicitud;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * Permite registrar o reemplazar documentos adicionales.
 * @author luisr.rodriguez
 */
@Path("/documentoAdicional")
@RequestScoped
public class CrearDocumentoAdicionalEndPoint extends BaseGUIEndPoint<BaseModel, 
        BaseModel, BaseModel>{
    @Inject
    private SolicitudBackService solicitudBackService;
    @Inject
    private DocumentoConsultaService consultarDocumento;
    @Inject
    private BovedaAssamber assembler;
    @Inject
    private BovedaService boveda;
    @Inject
    private DocumentoBajaService bajaDocumento;
    @Inject
    private DocumentoService documento;
    
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Registrar un documento",
            description = "Registrar un documento")
    public Response upload(MultipartFormDataInput input) 
            throws BusinessException {
        MultipartBody data = new MultipartBody(input);
        
        SolicitudRequest solicitudRequest = new SolicitudRequest();
        solicitudRequest.setId(Long.parseLong(data.getIdSolicitud()));
        Message<SolicitudRequest> responseSolicitud = solicitudBackService
                .execute(new Message<>(solicitudRequest));
        EstadoSolicitud estadoSolicitud = responseSolicitud.getPayload()
                .getSolicitudResponse().getCveEstadoSolicitud();
        log.log(Level.INFO, ">>>solicitudConsulta, verify: {0}", 
                responseSolicitud);
        
        DocumentoRequest request = assembler.assemble(data);
        
        /*
        *La carga de documentos adicionales es solo para solicitudes con estado:
        * Autorizada, Préstamo otorgado y Préstamo en recuperación.
        */
        if(estadoSolicitud.getId() == Long.parseLong("3") 
                || estadoSolicitud.getId() == Long.parseLong("8") 
                || estadoSolicitud.getId() == Long.parseLong("17")){
            Message<DocumentoRequest> resposeConsul = consultarDocumento
                    .consultarBySolicitudAndTipo(new Message<>(request), data.getIdSolicitud());
            log.log(Level.INFO, ">>>documentoFront upload, verify: {0}", resposeConsul);
            
            if (Message.isException(resposeConsul)) {
                log.log(Level.INFO, ">>>Existe el documento:{0}", resposeConsul);
                request.getDocumentoResponse().setRefDocumento(
                resposeConsul.getPayload().getDocumentoResponse().getRefDocumento());
                bajaDocumento.execute(new Message<>(resposeConsul.getPayload()));
            }
            
            log.log(Level.INFO, ">>>documentoFront upload, after verify");
            
            request = assembler.assemble(data);

            ServiceDefinition[] steps2 = {documento, boveda, documento};

            request.getDocumentoResponse().setId(null);
            request.getDocumentoResponse().setTipoDocumento(
                    request.getTipoDocumento()
            );
            request.getDocumentoResponse().setCveSolicitud(
                    Long.parseLong(request.getBovedaRequest().getTramite().getFolioTramite())
            );

            Message<DocumentoRequest> response = boveda.executeSteps(steps2, new Message<>(request));
            log.log(Level.INFO, ">>>Regresando documento almacenado: {0}", response.getPayload().getDocumentoResponse());
            return Response.ok(response.getPayload().getDocumentoResponse()).build();        
        }
        return Response.noContent().build();
    }
    
}
