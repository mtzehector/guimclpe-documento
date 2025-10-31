/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.documentofront.assember;


import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.documentofront.model.CreateDocumentReq;
import mx.gob.imss.dpes.documentofront.model.Documento;
import mx.gob.imss.dpes.documentofront.model.DocumentoRequest;
import mx.gob.imss.dpes.documentofront.model.MultipartBody;
import mx.gob.imss.dpes.documentofront.model.Tramite;
import mx.gob.imss.dpes.documentofront.model.Usuario;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class BovedaAssamber extends BaseAssembler<MultipartBody, DocumentoRequest> {

    
    @Override
    public DocumentoRequest assemble(MultipartBody data) {
        
        DocumentoRequest request = new DocumentoRequest();
        request.setBovedaRequest(new CreateDocumentReq());
        request.getBovedaRequest().setDocumento(new Documento());
        request.getBovedaRequest().getDocumento().setArchivo(data.getFile());
        request.getBovedaRequest().getDocumento().setNombreArchivo(data.getNombre());
        request.getBovedaRequest().getDocumento().setExtencion( data.getTipo().getMedia() );
        request.getBovedaRequest().getDocumento().setEFinanciera(data.getEFinanciera());
        request.getBovedaRequest().getDocumento().setCvePrestamoRecuperacion(data.getCvePrestamoRecuperacion());
        request.getBovedaRequest().setTramite(new Tramite() );
        request.getBovedaRequest().getTramite().setFolioTramite(data.getIdSolicitud());
        request.getBovedaRequest().setUsuario(new Usuario());
        request.getBovedaRequest().getUsuario().setIdUsr("1");
        request.getBovedaRequest().getUsuario().setTipoIdUsr("pensionado");
        request.getBovedaRequest().setSesion(data.getSesion());
        request.setTipoDocumento(data.getTipo());
        return request;
    }

}
