/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.documentofront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.PageModel;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.documentofront.assember.CepCompraAssembler;
import mx.gob.imss.dpes.documentofront.model.CepCompra;
import mx.gob.imss.dpes.documentofront.model.CepCompraRes;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;;
import mx.gob.imss.dpes.documentofront.restclient.CepCompraClient;
import mx.gob.imss.dpes.doumentofront.exception.DocumentoException;
import mx.gob.imss.dpes.interfaces.documento.model.TipoDocumentoFront;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juanf.barragan
 */
@Provider
public class ObtenerCepCompraService extends ServiceDefinition<CepCompra,CepCompra> {

    @Inject
    @RestClient
    private CepCompraClient client;
    
    @Inject
    private CepCompraAssembler assembler;
    
    @Override
    public Message<CepCompra> execute(Message<CepCompra> request) throws BusinessException {
        log.log(Level.INFO, ">>>documentofront|ObtenerCepCompraService|execute {0}", request.getPayload());
        
        Response response = client.load(request.getPayload().getCepRequest());
        
         if (response.getStatus() == 200) {
            return response(response, request);
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new DocumentoException(), null);
    }
    
    @Override
    protected Message<CepCompra> onOk(Response response, Message<CepCompra> request){
        PageModel<Documento> documento = response.readEntity(new GenericType<PageModel<Documento>>() {});
        
        for(Documento doc : documento.getContent()){
            doc.setDescTipoDocumento(doc.getTipoDocumento().getDescripcion());
            doc.setTipoDocumentoEnum(new TipoDocumentoFront(TipoDocumentoEnum.CEP_ENTIDAD_FINANCIERA));
        }
        
        PageModel<CepCompraRes> cepCompraRes = assembler.assemblePage(documento);
        
        request.getPayload().setCepResponse(cepCompraRes);
        
        return request;
    }
    
}
