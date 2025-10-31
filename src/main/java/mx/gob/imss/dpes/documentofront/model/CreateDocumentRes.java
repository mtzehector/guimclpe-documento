/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.documentofront.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author eduardo.loyo
 */
@Data
public class CreateDocumentRes extends BaseModel {
    private RespuestaBoveda respuestaBoveda = new RespuestaBoveda();
    private boolean exito;
    private String clave;
    private String descripcion;
    private String idDocumento;
    private String descripcionError;
}
