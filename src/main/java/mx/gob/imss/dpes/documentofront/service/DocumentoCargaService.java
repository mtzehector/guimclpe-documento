/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import mx.gob.imss.dpes.documentofront.model.CreateDocumentReq;
import mx.gob.imss.dpes.documentofront.model.DocumentoRequest;
import mx.gob.imss.dpes.documentofront.model.Tramite;
import mx.gob.imss.dpes.documentofront.restclient.DocumentoClient;
import mx.gob.imss.dpes.doumentofront.exception.DocumentoException;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author luisr.rodriguez
 */
@Provider
public class DocumentoCargaService extends ServiceDefinition<DocumentoRequest, DocumentoRequest> {
    @Inject
    @RestClient
    private DocumentoClient client;

    @Override
    /**
     * Busca un documento por su Id
     */
    public Message<DocumentoRequest> execute(Message<DocumentoRequest> request) throws BusinessException {
        Long idDocumento = request.getPayload().getDocumento().getId();
        log.log(Level.INFO, ">>>Id de documento:{0}", idDocumento);
        Response load = client.load(idDocumento);
        log.log(Level.INFO, ">>>Respuesta:{0}", load.getEntity());
        if (load.getStatus() == 200) {
            log.log(Level.INFO, "devolvio 200");
            Documento out = load.readEntity(Documento.class);
            log.log(Level.INFO,"Salida busqueda: {0}", out);
            request.getPayload().setDocumentoResponse(out);
            //Para eliminación
            CreateDocumentReq bovedaReq = new CreateDocumentReq();
            Tramite tramite = new Tramite();
            tramite.setFolioTramite(String.valueOf(out.getCveSolicitud()));
            bovedaReq.setTramite(tramite);
            request.getPayload().setBovedaRequest(bovedaReq);
            return response(request.getPayload(), ServiceStatusEnum.EXCEPCION, new DocumentoException(DocumentoException.NOTFOUND), null);
        }else if(load.getStatus() == 303 || load.getStatus() == 204){
            log.log(Level.INFO,"NO ENCONTRO MATCH");
            return request;
        }
        log.log(Level.INFO,"Busqueda exception: ");
        return response(request.getPayload(), ServiceStatusEnum.EXCEPCION, new DocumentoException(DocumentoException.NOTFOUND), null);
    }
    
}
