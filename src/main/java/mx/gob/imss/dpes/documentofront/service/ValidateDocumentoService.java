package mx.gob.imss.dpes.documentofront.service;

import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.documentofront.restclient.DocumentoClient;
import mx.gob.imss.dpes.doumentofront.exception.DocumentoException;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.logging.Level;

@Provider
public class ValidateDocumentoService extends ServiceDefinition<Documento, Documento> {

  @Inject
  @RestClient
  private DocumentoClient documentoClient;

  @Override
  public Message<Documento> execute(Message<Documento> request) {
    Response response = documentoClient.bySolicitudAndTipo(request.getPayload());
    if (response.getStatus() == 200) {
      try {
        Documento documento = response.readEntity(new GenericType<List<Documento>>() {
        }).get(0);
        log.log(Level.INFO, "Documento : {0}", documento);
        return new Message<>(documento);
      } catch (Exception e) {
        log.log(Level.SEVERE, null, e);
      }
    }
    return response(null, ServiceStatusEnum.EXCEPCION, new DocumentoException(DocumentoException.NOTFOUND), null);
  }

}