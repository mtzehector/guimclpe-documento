/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.documentofront.assember;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;


import mx.gob.imss.dpes.documentofront.model.DocumentoRequest;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class DocumentoFrontAssembler extends BaseAssembler<DocumentoRequest, Documento> {


    @Override
    public Documento assemble(DocumentoRequest source) {
        Documento doc = new Documento();
        doc.setId( source.getDocumentoResponse().getId() );
        doc.setCveSolicitud(Long.parseLong(source.getBovedaRequest().getTramite().getFolioTramite()));
        doc.setTipoDocumento(source.getTipoDocumento());
        return doc;
    }

}
