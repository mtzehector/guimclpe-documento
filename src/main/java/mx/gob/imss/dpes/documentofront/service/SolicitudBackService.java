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
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.documentofront.model.SolicitudRequest;
import mx.gob.imss.dpes.documentofront.restclient.SolicitudBackClient;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author luisr.rodriguez
 */
@Provider
public class SolicitudBackService extends ServiceDefinition<SolicitudRequest, SolicitudRequest> {
    @Inject
    @RestClient
    private SolicitudBackClient SolicitudClient;
    
    @Override
    public Message<SolicitudRequest> execute(Message<SolicitudRequest> request) throws BusinessException {
        log.log(Level.INFO, "******************************Step Obteniendo Solicitud******************************************");
        log.log(Level.INFO, "Inicia Solicitud");
        Response response = SolicitudClient.getSolicitud(request.getPayload().getId());
        if (response.getStatus() == 200) {
            log.log(Level.INFO, "devolvio 200");
            try{
                Solicitud solicitud = response.readEntity(Solicitud.class);
                request.getPayload().setSolicitudResponse(solicitud);
            }catch(Exception e){
                request.getPayload().setId(null);
            }
        }else if(response.getStatus() == 303 || response.getStatus() == 204){
            log.log(Level.INFO,"NO ENCONTRO MATCH");
            request.getPayload().setId(null);
        }
        return request;
    }
    
}
