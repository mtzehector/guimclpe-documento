/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.documentofront.service;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.documentofront.model.CepCompra;
import mx.gob.imss.dpes.documentofront.model.CepCompraRes;
import mx.gob.imss.dpes.documentofront.model.PrestamoRecuperacionRs;
import mx.gob.imss.dpes.documentofront.restclient.PensionadoClient;
import mx.gob.imss.dpes.documentofront.restclient.PrestamoRecuperacionClient;
import mx.gob.imss.dpes.documentofront.restclient.SolicitudBackClient;
import mx.gob.imss.dpes.interfaces.persona.model.Pensionado;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juanf.barragan
 */
@Provider
public class ConsultaCepCompraService extends ServiceDefinition<CepCompra,CepCompra>{

    
    @Inject
    @RestClient
    private SolicitudBackClient solicitud;
    
    @Inject
    @RestClient
    private PrestamoRecuperacionClient prestamoRec;
    
    @Inject
    @RestClient
    private PensionadoClient pensionado;
    
    @Override
    public Message<CepCompra> execute(Message<CepCompra> request) throws BusinessException {
        log.log(Level.INFO, "INICIA LA CONSULTA DE LA INFORMACION ADICIONAL");
        
        for(CepCompraRes resp : request.getPayload().getCepResponse().getContent()){
            Response respSolicitud  = solicitud.getSolicitud(resp.getDocumento().getCveSolicitud());
            log.log(Level.INFO, "Termina consulta de solicitud");
            Solicitud sol = respSolicitud.readEntity(Solicitud.class);
            
            Response respPrestamo = prestamoRec.consultaPrestamosPorSolicitud(sol.getId());
            log.log(Level.INFO, "Termina consulta de prestamo recuperacion");
            Response respPersona = pensionado.getPersonaByCurpNss(sol.getCurp(), sol.getNss());
            log.log(Level.INFO, "Termina consulta de pensionado");
            Pensionado persona = respPersona.readEntity(Pensionado.class);
            
            //PrestamoRecuperacion prestamo = respPrestamo.readEntity(PrestamoRecuperacion.class);
            
            resp.setFolio(sol.getNumFolioSolicitud());
            resp.setNombre(persona.getNombre()+" "+persona.getPrimerApellido()+" "+persona.getSegundoApellido());
            resp.setNss(sol.getNss());
           
            if(respPrestamo.getStatus() == 200){
                PrestamoRecuperacionRs recu = respPrestamo.readEntity(PrestamoRecuperacionRs.class);
                if(resp.getDocumento().getCvePrestamoRecuperacion() != null){
                    for(PrestamoRecuperacion rs : recu.getPrestamosEnRecuperacion()){
                        if(!resp.getDocumento().getCvePrestamoRecuperacion().toString().equals(rs.getId().toString())){
                            log.log(Level.INFO, "No corresponde al prestamo");
                        }else{
                            resp.setReferencia(rs.getReferencia());
                            resp.setSCapital(rs.getCanMontoSol());
                        }
                    }
                }
            }
            
        }
        log.log(Level.INFO, "TERMINA LA CONSULTA DE LA INFORMACION ADICIONAL");
        
        return request;
    }
    
}
