package mx.gob.imss.dpes.documentofront.service;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
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
public class DocumentoConsultaService extends ServiceDefinition<DocumentoRequest, DocumentoRequest> {
    
    @Inject
    @RestClient
    private DocumentoClient client;
    @Inject
    private DocumentoFrontAssembler assembler;
   
    
    @Override
    public Message<DocumentoRequest> execute(Message<DocumentoRequest> request) throws BusinessException {
        log.log(Level.INFO, "******************************Step 1 consulta de documento******************************************");
        log.log(Level.INFO, "Inicia Búsqueda de documento: {0}", request);
        Documento documento = assembler.assemble(request.getPayload());
        Response load = client.loadDocumento(documento);
        if (load.getStatus() == 200) {
            log.log(Level.INFO, "devolvio 200");
            Documento out = load.readEntity(Documento.class);
            log.log(Level.INFO,"Salida busqueda: {0}", out);
            request.getPayload().setDocumentoResponse(out);
            
            return response(request.getPayload(), ServiceStatusEnum.EXCEPCION, new DocumentoException(DocumentoException.NOTFOUND), null);
        }else if(load.getStatus() == 303 || load.getStatus() == 204){
            log.log(Level.INFO,"NO ENCONTRO MATCH");
            return request;
        }
        log.log(Level.INFO,"Busqueda exception: ");
        return response(request.getPayload(), ServiceStatusEnum.EXCEPCION, new DocumentoException(DocumentoException.NOTFOUND), null);
    }

    public Message<DocumentoRequest> consultarBySolicitudAndTipo(
            Message<DocumentoRequest> request, String idSolicitud) throws BusinessException {
        log.log(Level.INFO, "Inicia Búsqueda de documentos por solicitud y tipo: {0}", request);
        Documento documentoRequest = assembler.assemble(request.getPayload());
        documentoRequest.setCveSolicitud(Long.parseLong(idSolicitud));
        
        Response load = client.bySolicitudAndTipo(documentoRequest); 
        if (load.getStatus() == 200) {
            log.log(Level.INFO, "devolvio 200");
            List<Documento> listaDocumentos = load.readEntity(new GenericType<List<Documento>>(){});
            log.log(Level.INFO,"Salida busqueda: {0}", listaDocumentos);
            
            List<Documento> listaDocumentosBoveda = listaDocumentos.stream()
                    .filter(documento -> documento.getRefDocBoveda() != null)
                    .collect(Collectors.toList());
            
            if(listaDocumentosBoveda.isEmpty() || listaDocumentosBoveda.size() == 1){
                log.log(Level.INFO,"Primer Documento o Documento Adicional");
                return request;
            }else{
                log.log(Level.INFO,"Existe Documento Adicional");
                Documento documentoResponse = new Documento();
                documentoRequest.setId(Long.MIN_VALUE);
                for(Documento documento: listaDocumentosBoveda){
                    if(documento.getId() > documentoResponse.getId()){
                        documentoResponse = documento;
                    }
                }
                request.getPayload().setDocumentoResponse(documentoResponse);
                return response(request.getPayload(), ServiceStatusEnum.EXCEPCION, 
                        new DocumentoException(DocumentoException.NOTFOUND), null);
            }
        }else if(load.getStatus() == 303 || load.getStatus() == 204){
            log.log(Level.INFO,"NO ENCONTRO MATCH");
            return request;
        }
        log.log(Level.INFO,"Busqueda exception: ");
        return response(request.getPayload(), ServiceStatusEnum.EXCEPCION, new DocumentoException(DocumentoException.NOTFOUND), null);
    }
}
