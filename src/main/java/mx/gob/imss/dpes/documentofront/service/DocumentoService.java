/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.documentofront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.documentofront.model.DocumentoRequest;
import mx.gob.imss.dpes.documentofront.restclient.DocumentoClient;
import mx.gob.imss.dpes.doumentofront.exception.DocumentoException;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class DocumentoService extends ServiceDefinition<DocumentoRequest, DocumentoRequest> {

    @Inject
    @RestClient
    private DocumentoClient service;

    @Override
    public Message<DocumentoRequest> execute(Message<DocumentoRequest> request) throws BusinessException {
        log.log(Level.INFO, "******************************Step Guardado en BTDU******************************************");
        log.log(Level.INFO, "Inicia Documento BDTU");
        //se inserta solicitud

        log.log(Level.INFO, "Request Documento: {0}", request.getPayload());
        Documento documentoIn = new Documento();
        documentoIn.setCveSolicitud(Long.parseLong(request.getPayload().getBovedaRequest().getTramite().getFolioTramite()));
        
        documentoIn.setTipoDocumento( request.getPayload().getTipoDocumento() );
        
        documentoIn.setDescTipoDocumento(request.getPayload().getBovedaResponse().getDescripcion());
        documentoIn.setRefDocBoveda(request.getPayload().getBovedaResponse().getRespuestaBoveda().getIdDocumento());
        if(documentoIn.getTipoDocumento().getDescripcion().equalsIgnoreCase("CEP Entidad Financiera")){
            documentoIn.setEFinanciera(request.getPayload().getBovedaRequest().getDocumento().getEFinanciera());
            documentoIn.setCvePrestamoRecuperacion(request.getPayload().getBovedaRequest().getDocumento().getCvePrestamoRecuperacion());
        }
        Response load = service.create(documentoIn);
        if (load.getStatus() == 200) {
            log.log(Level.INFO, "devolvio 200");                        
               request.getPayload().setDocumentoResponse( load.readEntity(Documento.class) );
            return request;
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new DocumentoException(), null);
    }

}
