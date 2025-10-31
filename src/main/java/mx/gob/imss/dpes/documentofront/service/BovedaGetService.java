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
import mx.gob.imss.dpes.documentofront.model.CreateDocumentReq;
import mx.gob.imss.dpes.documentofront.model.CreateDocumentRes;
import mx.gob.imss.dpes.documentofront.model.DocumentoRequest;
import mx.gob.imss.dpes.documentofront.restclient.BovedaClient;
import mx.gob.imss.dpes.doumentofront.exception.DocumentoException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class BovedaGetService extends ServiceDefinition<CreateDocumentReq, CreateDocumentReq> {

    @Inject
    @RestClient
    private BovedaClient service;

    @Override
    public Message<CreateDocumentReq> execute(Message<CreateDocumentReq> request) throws BusinessException {
        log.log(Level.INFO, "******************************Step Obtener documento******************************************");
        log.log(Level.INFO, "Inicia Boveda obtención");
        log.log(Level.INFO, "Request hacia Boveda download: {0}", request.getPayload());
        
        Response load;
        
        if (request.getPayload().isIndDocumentoHistorico()) {
            log.log(Level.INFO, "########## Es un documento historico y la solicitud es a la boveda V2 ##########");
            load = service.load(request.getPayload());
        } else {
            log.log(Level.INFO, "########## No es un documento historico y la solicitud es a la boveda V3 ##########");
            load = service.loadBovedaV3(request.getPayload());
        }
        
        if (load.getStatus() == 200) {
            log.log(Level.INFO,"Respondio 200 ok ");
            CreateDocumentRes bovedaOut = load.readEntity(CreateDocumentRes.class);
            request.getPayload().getDocumento().setArchivo(bovedaOut.getRespuestaBoveda().getArchivo());
            request.getPayload().getDocumento().setNombreArchivo(bovedaOut.getRespuestaBoveda().getDescripcion());
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new DocumentoException(), null);
    }

}
