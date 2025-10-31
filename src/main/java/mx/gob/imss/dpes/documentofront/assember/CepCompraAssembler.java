/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.documentofront.assember;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.documentofront.model.CepCompraRes;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;

/**
 *
 * @author juanf.barragan
 */
@Provider
public class CepCompraAssembler extends BaseAssembler<Documento,CepCompraRes>{

    @Override
    public CepCompraRes assemble(Documento source) {
        
        CepCompraRes res = new CepCompraRes();
        res.setDocumento(source);
        
        return res;
    }
    
}
