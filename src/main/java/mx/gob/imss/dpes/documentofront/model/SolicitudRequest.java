/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.gob.imss.dpes.documentofront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;

/**
 *
 * @author luisr.rodriguez
 */
public class SolicitudRequest extends BaseModel{
    @Getter
    @Setter
    Long id;
    @Getter
    @Setter
    Solicitud solicitudResponse = new Solicitud();
}
