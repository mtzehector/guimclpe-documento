/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.documentofront.config;

/**
 *
 * @author osiris.hernandez
 */
import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {

        resources.add(mx.gob.imss.dpes.common.exception.AlternateFlowMapper.class);
        resources.add(mx.gob.imss.dpes.common.exception.BusinessMapper.class);
        resources.add(mx.gob.imss.dpes.common.rule.MontoTotalRule.class);
        resources.add(mx.gob.imss.dpes.common.rule.PagoMensualRule.class);
        resources.add(mx.gob.imss.dpes.documentofront.assember.BovedaAssamber.class);
        resources.add(mx.gob.imss.dpes.documentofront.assember.CepCompraAssembler.class);
        resources.add(mx.gob.imss.dpes.documentofront.assember.DocumentoFrontAssembler.class);
        resources.add(mx.gob.imss.dpes.documentofront.endpoint.BajaDocumentoEndPoint.class);
        resources.add(mx.gob.imss.dpes.documentofront.endpoint.CrearDocumentoAdicionalEndPoint.class);
        resources.add(mx.gob.imss.dpes.documentofront.endpoint.CrearDocumentoEndPoint.class);
        resources.add(mx.gob.imss.dpes.documentofront.endpoint.ObtenerCepCompraEndPoint.class);
        resources.add(mx.gob.imss.dpes.documentofront.endpoint.ObtenerDocumentoEndPoint.class);
        resources.add(mx.gob.imss.dpes.documentofront.endpoint.ValidarDocumentoEndPoint.class);
        resources.add(mx.gob.imss.dpes.documentofront.service.BovedaGetService.class);
        resources.add(mx.gob.imss.dpes.documentofront.service.BovedaService.class);
        resources.add(mx.gob.imss.dpes.documentofront.service.ConsultaCepCompraService.class);
        resources.add(mx.gob.imss.dpes.documentofront.service.DocumentoBajaService.class);
        resources.add(mx.gob.imss.dpes.documentofront.service.DocumentoCargaService.class);
        resources.add(mx.gob.imss.dpes.documentofront.service.DocumentoConsultaService.class);
        resources.add(mx.gob.imss.dpes.documentofront.service.DocumentoService.class);
        resources.add(mx.gob.imss.dpes.documentofront.service.ObtenerCepCompraService.class);
        resources.add(mx.gob.imss.dpes.documentofront.service.SolicitudBackService.class);
        resources.add(mx.gob.imss.dpes.documentofront.service.ValidateDocumentoService.class);
    }

}
