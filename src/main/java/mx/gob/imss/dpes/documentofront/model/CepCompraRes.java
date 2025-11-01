/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.documentofront.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;


/**
 *
 * @author juanf.barragan
 */
@Data
public class CepCompraRes extends BaseModel{
    
    private String folio;
    private String nss;
    private String nombre;
    private String fechaCarga;
    private Double sCapital;
    private String referencia;
    private Documento documento;
    
}
