/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.documentofront.model;

import lombok.Data;

/**
 *
 * @author eduardo.loyo
 */
@Data
public class ReporteRespuesta {
    private byte[] archivo;
    private String solicitud;
    private String nombreArchivo;
    private String asegurado;
    private String idDocumento;
    
}
