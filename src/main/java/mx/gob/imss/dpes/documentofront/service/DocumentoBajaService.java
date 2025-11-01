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
import mx.gob.imss.dpes.documentofront.assember.DocumentoFrontAssembler;

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
public class DocumentoBajaService extends ServiceDefinition<DocumentoRequest, DocumentoRequest> {
    @Inject
    @RestClient
    private DocumentoClient client;
    @Inject 
    private DocumentoFrontAssembler assembler;
   
    @Override
    public Message<DocumentoRequest> execute(Message<DocumentoRequest> request) throws BusinessException {
        log.log(Level.INFO, "******************************Step baja de documento logica ******************************************");
        log.log(Level.INFO, "Inicia baja logica documento");
        Documento documento = assembler.assemble(request.getPayload());
        Response load = client.bajaLogica(documento.getId());
        if (load.getStatus() == 200) {
            log.log(Level.INFO, "devolvio 200");
            
            return request;
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new DocumentoException(), null);
    }
    
}
