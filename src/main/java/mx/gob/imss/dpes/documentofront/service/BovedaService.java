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
import mx.gob.imss.dpes.documentofront.model.Documento;
import mx.gob.imss.dpes.documentofront.model.DocumentoRequest;
import mx.gob.imss.dpes.documentofront.model.RespuestaBoveda;
import mx.gob.imss.dpes.documentofront.model.Tramite;
import mx.gob.imss.dpes.documentofront.model.Usuario;
import mx.gob.imss.dpes.documentofront.restclient.BovedaClient;
import mx.gob.imss.dpes.doumentofront.exception.DocumentoException;

import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class BovedaService extends ServiceDefinition<DocumentoRequest, DocumentoRequest> {

    @Inject
    @RestClient
    private BovedaClient service;

    @Override
    public Message<DocumentoRequest> execute(Message<DocumentoRequest> request) throws BusinessException {
        log.log(Level.INFO, "******************************Step boveda del Servicio******************************************");
        log.log(Level.INFO, ">>>>>Inicia Boveda");
        //se inserta solicitud
        CreateDocumentReq bovedaIn = new CreateDocumentReq();
        bovedaIn.setUsuario(new Usuario());
        bovedaIn.getUsuario().setIdUsr(request.getPayload().getBovedaRequest().getUsuario().getIdUsr());
        bovedaIn.setTramite(new Tramite());
        bovedaIn.getTramite().setFolioTramite(String.valueOf(request.getPayload().getDocumentoResponse().getId()));
        bovedaIn.setDocumento(new Documento());
        bovedaIn.getDocumento().setNombreArchivo(request.getPayload().getBovedaRequest().getDocumento().getNombreArchivo());
        bovedaIn.getDocumento().setExtencion(".pdf");
        bovedaIn.getDocumento().setArchivo(request.getPayload().getBovedaRequest().getDocumento().getArchivo());
        bovedaIn.setSesion(request.getPayload().getBovedaRequest().getSesion());
        log.log(Level.INFO, "*****************************Data file size [BovedaService Front]: {0}", bovedaIn.getDocumento().getArchivo());

        log.log(Level.INFO, ">>>>Request hacia Boveda: {0}", bovedaIn);
        Response load = null;
        try {
            load = service.create(bovedaIn);
        } catch (Exception e) {
            log.log(Level.SEVERE, ">>>>ERROR!!! Request hacia Boveda={0}",e.getMessage());
            e.printStackTrace();
        }
        if (load != null && load.getStatus() == 200) {
            CreateDocumentRes bovedaOut = load.readEntity(CreateDocumentRes.class);
            request.getPayload().setBovedaResponse(bovedaOut);
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new DocumentoException(), null);
    }

}
