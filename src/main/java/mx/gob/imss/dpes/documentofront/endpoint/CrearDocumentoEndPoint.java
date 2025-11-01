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
import org.eclipse.microprofile.openapi.annotations.Operation;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.documentofront.assember.BovedaAssamber;
import mx.gob.imss.dpes.documentofront.model.DocumentoRequest;
import mx.gob.imss.dpes.documentofront.model.MultipartBody;
import mx.gob.imss.dpes.documentofront.restclient.DocumentoClient;
import mx.gob.imss.dpes.documentofront.service.BovedaService;
import mx.gob.imss.dpes.documentofront.service.DocumentoBajaService;
import mx.gob.imss.dpes.documentofront.service.DocumentoConsultaService;
import mx.gob.imss.dpes.documentofront.service.DocumentoService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 *
 * @author antonio
 */
@Path("/documento")
@RequestScoped
public class CrearDocumentoEndPoint extends BaseGUIEndPoint<BaseModel, BaseModel, BaseModel> {

    @Inject
    private DocumentoConsultaService consultarDocumento;//Step 1
    @Inject
    private DocumentoBajaService bajaDocumento; //Step1a

    @Inject
    private BovedaService boveda; //Step 2
    @Inject
    private DocumentoService documento; //Step 3

    @Inject
    @RestClient
    DocumentoClient documentoClient;

    @Inject
    private BovedaAssamber assembler;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Registrar un documento", description = "Registrar un documento")
    public Response upload(MultipartFormDataInput input) throws BusinessException {

    	log.log(Level.INFO, "########## URL invocada [/documento] CrearDocumentoEndPoint del proyecto documentoFront ##########");
        log.log(Level.INFO, ">>> CrearDocumentoEndPointData input={0}", input);

        MultipartBody data = new MultipartBody(input);
        log.log(Level.INFO, ">>>*************Data file size [CrearDocumentoEndPoint]:{0}", data.getFile().length);

        DocumentoRequest request = assembler.assemble(data);

        ServiceDefinition[] steps1 = {consultarDocumento};
        Message<DocumentoRequest> resposeConsul = consultarDocumento.executeSteps(steps1, new Message<>(request));
        log.log(Level.INFO, ">>>documentoFront upload, verify: {0}", resposeConsul);

        if (Message.isException(resposeConsul)) {
            log.log(Level.INFO, ">>>Existe el documento:{0}", resposeConsul);
            request.getDocumentoResponse().setRefDocumento(
                    resposeConsul.getPayload().getDocumentoResponse().getRefDocumento()
            );
            ServiceDefinition[] steps1a = {bajaDocumento};
            bajaDocumento.executeSteps(steps1a, new Message<>(resposeConsul.getPayload()));
        }
        log.log(Level.INFO, ">>>documentoFront upload, after verify");
            
        request = assembler.assemble(data);

        ServiceDefinition[] steps2 = {documento, boveda, documento};

        request.getDocumentoResponse().setId(null);
        request.getDocumentoResponse().setTipoDocumento(request.getTipoDocumento());
        request.getDocumentoResponse().setCveSolicitud(
                Long.parseLong(request.getBovedaRequest().getTramite().getFolioTramite())
        );

        Message<DocumentoRequest> response = boveda.executeSteps(steps2, new Message<>(request));
        log.log(Level.INFO, ">>>Regresando documento almacenado: {0}", response.getPayload().getDocumentoResponse());
        return Response.ok(response.getPayload().getDocumentoResponse()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/refDocumento/bySolicitudAndTipo")
    public Response loadRefDocumento(mx.gob.imss.dpes.interfaces.documento.model.Documento request) throws BusinessException {
        return this.documentoClient.loadRefDocumento(request);
    }

}
