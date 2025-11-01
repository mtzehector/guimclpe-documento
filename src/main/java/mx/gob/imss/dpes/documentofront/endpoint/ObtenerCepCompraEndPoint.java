/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import mx.gob.imss.dpes.common.model.PageRequestModel;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.documentofront.model.CepCompra;
import mx.gob.imss.dpes.documentofront.model.CepCompraReq;
import mx.gob.imss.dpes.documentofront.service.ConsultaCepCompraService;
import mx.gob.imss.dpes.documentofront.service.ObtenerCepCompraService;

/**
 *
 * @author juanf.barragan
 */
@Path("/obtenerCepCompra")
@RequestScoped
public class ObtenerCepCompraEndPoint extends BaseGUIEndPoint<BaseModel, BaseModel, BaseModel>{
    
    @Inject 
    ObtenerCepCompraService obtenerCepCompraService;
    @Inject 
    ConsultaCepCompraService consultaCepCompraService; 
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)  
    public Response ObtenerCepCompra (PageRequestModel<CepCompraReq> request) throws BusinessException {
        log.log(Level.INFO, ">>>documentofront|ObtenerCepCompraEndPoint|ObtenerCepCompra {0}" + request);
        
        CepCompra cepCompra = new CepCompra();
        cepCompra.setCepRequest(request);
        
        ServiceDefinition[] steps = {obtenerCepCompraService,consultaCepCompraService};
        
        Message<CepCompra> response = obtenerCepCompraService.executeSteps(steps, new Message<>(cepCompra));
        
        if (Message.isException(response)) {
            return toResponse(response);
        }
        return Response.ok( response.getPayload().getCepResponse() ).build();
        
    } 
    
}
