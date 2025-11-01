package mx.gob.imss.dpes.documentofront.model;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.BaseModel;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 *
 * @author antonio
 */
@NoArgsConstructor
public class MultipartBody extends BaseModel{  
  @Getter @Setter byte[] file;  
  @Getter @Setter String nombre;  
  @Getter @Setter TipoDocumentoEnum tipo;
  @Getter @Setter String idSolicitud;
  @Getter @Setter String eFinanciera;
  @Getter @Setter Long cvePrestamoRecuperacion;
  @Getter @Setter Long sesion;
  protected final Logger log = Logger.getLogger( getClass().getName() );
  
  public MultipartBody(MultipartFormDataInput input) throws BusinessException{
      try{
        
          Map<String, List<InputPart>> formDataMap = input.getFormDataMap();
            List<InputPart> list;
            list = formDataMap.get("tipo");
            tipo = list != null && list.size() == 1 ? TipoDocumentoEnum.forValue(Long.parseLong(list.get(0).getBodyAsString())) : null;
            
            list = formDataMap.get("file");
            if (list != null && list.size() == 1) {
                InputPart ip = list.get(0);
                log.log(Level.INFO, ">>>ip.getMediaType().toString()={0}", ip.getMediaType().toString()+"   data.getTipo().getMedia()="+tipo.getMedia());
                byte[] archivo = ip.getBody(byte[].class, null);
                if (!ip.getMediaType().toString().contains(tipo.getMedia())) {
                    throw new BusinessException("invalidFile");
                }
                file = archivo;
            }
            
            if(RevisaNombre(formDataMap.get("nombre").get(0))){
                throw new BusinessException("invalidName");
            }
            
            list = formDataMap.get("idSolicitud");
            idSolicitud = list != null && list.size() == 1 ? list.get(0).getBodyAsString() : "";
            if(tipo.getDescripcion().equalsIgnoreCase("CEP Entidad Financiera")){
                list = formDataMap.get("eFinanciera");
                eFinanciera = list != null && list.size() == 1 ? list.get(0).getBodyAsString() : "";
                
                list = formDataMap.get("cvePrestamoRecuperacion");
                cvePrestamoRecuperacion = list != null && list.size() == 1 ? Long.parseLong(list.get(0).getBodyAsString()) : null;
            }
            list = formDataMap.get("nombre");
            nombre = list != null && list.size() == 1 ? list.get(0).getBodyAsString() : "";
            log.log(Level.INFO, ">>>*************Data file size: {0}", file.length);

            list = formDataMap.get("sesion");
            sesion = list != null && list.size() == 1 ? Long.parseLong(list.get(0).getBodyAsString()) : null;
      }catch(IOException ex) {
            log.log(Level.SEVERE, null, ex);
            throw new BusinessException("invalidFile");
      }
  }
  
  private boolean RevisaNombre (InputPart ip){
      try{
          String ipBodyAsString = ip.getBodyAsString();
          return ipBodyAsString.contains("(") || ipBodyAsString.contains(")");
      }catch(IOException e){
          log.log(Level.SEVERE, "Error al evaluar el nombre del archivo", e);
          return true;
      }
  }
}
