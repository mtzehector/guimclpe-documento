/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.documentofront.endpoint;

import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.documentofront.model.CreateDocumentReq;
import mx.gob.imss.dpes.documentofront.restclient.DocumentoClient;
import mx.gob.imss.dpes.documentofront.service.BovedaGetService;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Path("/obtener")
@RequestScoped
public class ObtenerDocumentoEndPoint extends BaseGUIEndPoint<CreateDocumentReq, CreateDocumentReq, CreateDocumentReq> {

    private static final String PDF_EXTENSION = "pdf";
    private static final String PDF_MIME_TYPE = "application/pdf";
    private static final String XML_EXTENSION = "xml";
    private static final String XML_MIME_TYPE = MediaType.APPLICATION_XML;
    private static final String XLSX_EXTENSION = "xlsx";
    private static final String XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String TXT_EXTENSION = "txt";
    private static final String TXT_MIME_TYPE = "text/plain";
    private String cabecera;
    private String mime;

    @Inject
    private BovedaGetService boveda;

    @Inject
    @RestClient
    DocumentoClient documentoClient;

    @Path("/{refDocumento}")
    @GET    
    @Operation(summary = "Generar el reporte de resumen de carta de libranza",
            description = "Generar el reporte de resumen de carta de libranza")
    public Response create(@PathParam("refDocumento") long id) throws BusinessException {
        log.log(Level.INFO, "########## URL invocada [/obtener/{refDocumento}] ObtenerDocumentoEndPoint del proyecto documentoFront ##########");
        log.log(Level.INFO, "Inicia obtención de documento con del id: {0}", id);

        Response responseLoad = documentoClient.load(id);
        
        if (responseLoad.getStatus() == 200) {
            Documento documento  = responseLoad.readEntity(Documento.class);
            log.log(Level.INFO, "Resultado busqueda de documento front: {0}", documento);

            switch (documento.getTipoDocumento().getTipo().intValue()) {
                case 12:
                case 13:
                case 16:
                    cabecera = XML_EXTENSION;
                    mime = XML_MIME_TYPE;
                    break;
                case 17:
                    cabecera = XLSX_EXTENSION;
                    mime = XLSX_MIME_TYPE;
                    break;
                case 18:
                    cabecera = TXT_EXTENSION;
                    mime = TXT_MIME_TYPE;
                    break; 
                case 19:
                    cabecera = XLSX_EXTENSION;
                    mime = XLSX_MIME_TYPE;
                    break;            
                case 20:
                    cabecera = XLSX_EXTENSION;
                    mime = XLSX_MIME_TYPE;
                    break;
                case 21:
                    cabecera = TXT_EXTENSION;
                    mime = TXT_MIME_TYPE;
                    break;
                case 22:
                    cabecera = TXT_EXTENSION;
                    mime = TXT_MIME_TYPE;
                    break;
                case 23:
                    cabecera = XLSX_EXTENSION;
                    mime = XLSX_MIME_TYPE;
                    break;
                default:
                    cabecera = PDF_EXTENSION;
                    mime = PDF_MIME_TYPE;
                    break;
            }
            
            CreateDocumentReq request = new CreateDocumentReq();
            request.getDocumento().setIdDocumento(documento.getRefDocBoveda());
            request.setIndDocumentoHistorico(documento.isIndDocumentoHistorico());

            Message<CreateDocumentReq> response = boveda.execute(new Message<>(request));

            if (!Message.isException(response)) {
                String nombreArchivo = response.getPayload().getDocumento().getNombreArchivo();
              //  log.log(Level.INFO, "Respuesta boveda front: {0}", response);
                String filename = nombreArchivo.split("\\.")[0];
                return Response.ok(response.getPayload().getDocumento().getArchivo()).header("Content-Disposition",
                        "attachment; filename=" + filename + "." + cabecera).header("Content-Type", mime).build();
            }
        }

        return Response.noContent().build();
    }
    
    
    @Path("/img/{refDocumento}")
    @GET    
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerImg(@PathParam("refDocumento") long id) throws BusinessException {
        log.log(Level.INFO, "########## URL invocada [/obtener/img/{refDocumento}] ObtenerDocumentoEndPoint del proyecto documentoFront ##########");
        log.log(Level.INFO, "Inicia obtencion de documento con del id: {0}", id);

        Response responseLoad = documentoClient.load(id);
        
        if (responseLoad.getStatus() == 200) {
            Documento documento  = responseLoad.readEntity(Documento.class);
            log.log(Level.INFO, "Resultado busqueda de documento front: {0}", documento);
            
            CreateDocumentReq request = new CreateDocumentReq();
            request.getDocumento().setIdDocumento(documento.getRefDocBoveda());
            request.setIndDocumentoHistorico(documento.isIndDocumentoHistorico());

            Message<CreateDocumentReq> response = boveda.execute(new Message<>(request));

            if (!Message.isException(response)) {
            //    log.log(Level.INFO, "Respuesta boveda front: {0}", response);
                return Response.ok(response.getPayload().getDocumento()).build();
            }
        }
        return Response.noContent().build();
    }
}
