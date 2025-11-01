/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.documentofront.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;

/**
 *
 * @author eduardo.loyo
 */
@Data
public class DocumentoRequest extends BaseModel {

    @Getter
    @Setter
    private CreateDocumentReq bovedaRequest = new CreateDocumentReq();
    @Getter
    @Setter
    private CreateDocumentRes bovedaResponse = new CreateDocumentRes();
    @Getter
    @Setter
    private Documento documento = new Documento();
    @Getter
    @Setter
    private TipoDocumentoEnum tipoDocumento;
    @Getter
    @Setter
    Documento documentoResponse = new Documento();
}
